package com.angiuprojects.dispensav2.queries

import android.annotation.SuppressLint
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import com.angiuprojects.dispensav2.entities.ItemDTO
import com.angiuprojects.dispensav2.entities.ItemInterface
import com.angiuprojects.dispensav2.entities.Profile
import com.angiuprojects.dispensav2.entities.Section
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.entities.WhereCondition
import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.enums.StorageItemColumnEnum
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.reflect.Method
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.reflect.KFunction2
import kotlin.text.StringBuilder


class Queries {

    companion object {

        val DB_INSTANCE: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://dispensa-v2-default-rtdb.firebaseio.com/")

        const val STORAGE_ITEMS_DB_REFERENCE = "StorageItems"
        const val HISTORY_ITEMS_DB_REFERENCE = "HistoryItems"
        private lateinit var myRef: DatabaseReference

        lateinit var singleton: Queries

        fun initializeQueries() {
            singleton = Queries()
        }
    }

    private lateinit var conn: Connection

    fun insertItem(storageItem: StorageItem) : Int{
        return try {
            executeInsertUpdate(insertQueryBuilder("storage", storageItem))
        } catch(e: Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Error inserting storage item " + e.message)
            0
        }
    }

    fun updateItem(newItem: StorageItem, oldItem: StorageItem) {
        try {
            executeInsertUpdate(updateQueryBuilder("storage", newItem, oldItem)) //todo check res
        } catch(e: Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Error updating storage item " + e.message)
        }
    }

    fun selectItemsQuery(whereConditions: MutableList<WhereCondition>) {
        executeSelect(selectQueryBuilder("storage", whereConditions), Queries::selectItems)
    }

    private fun selectItems(res: ResultSet?) {
        Constants.itemMap = mutableMapOf()
        if(res != null) {
            while (res.next()) {
                try{
                    val section = Utils.singleton.findByName(res.getString(2), Constants.sectionList)
                    val profile = Utils.singleton.findByName(res.getString(3), Constants.profileList)

                    if(res.getString(1) == null || res.getString(1).isEmpty()) throw Exception("Invalid Name!")
                    if(section == null) throw Exception("Invalid Section!")
                    if(profile == null) throw Exception("Invalid Profile!")

                    val si = StorageItem(res.getString(1),
                        section,
                        profile,
                        res.getInt(4),
                        res.getInt(5),
                        res.getDate(6))

                    Constants.itemMap[si.name] = si
                    Log.i(Constants.STORAGE_LOGGER, "Retrieved StorageItem: " + si.name)
                } catch (e: Exception) {
                    Log.e(Constants.STORAGE_LOGGER, "Error converting DB result to StorageItem " + e.message)
                }
            }
        }
    }

    fun selectProfiles(res: ResultSet?) {
        Constants.profileList = mutableListOf()
        if(res != null) {
            while (res.next()) {
                try{
                    val p = Profile(res.getString(1), ProfileButtonStateEnum.getProfileButtonStateEnum(res.getString(2)),
                        res.getString(3))
                    Constants.profileList.add(p)
                    Log.i(Constants.STORAGE_LOGGER, "Retrieved Profile: " + p.name)
                } catch (e: Exception) {
                    Log.e(Constants.STORAGE_LOGGER, "Error converting DB result to Profile " + e.message)
                }
            }
        }
    }

    fun selectSections(res: ResultSet?) {
        Constants.sectionList = mutableListOf()
        if(res != null) {
            while (res.next()) {
                try{
                    val s = Section(res.getString(1))
                    Log.i(Constants.STORAGE_LOGGER, "Retrieved Section: " + s.name)
                    Constants.sectionList.add(s)
                } catch (e: Exception) {
                    Log.e(Constants.STORAGE_LOGGER, "Error converting DB result to Section " + e.message)
                }
            }
        }
    }

    fun connectToDB() {
        Class.forName("net.sourceforge.jtds.jdbc.Driver").getDeclaredConstructor().newInstance()
        val connString =
            "jdbc:jtds:sqlserver://storageserverdatabase.database.windows.net:1433/storage_db;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;ssl=request;loginTimeout=30;"
        val username = "storage_db@storageserverdatabase"
        val password = "F4cc1n4s0rr1d3nt3!"

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        conn = DriverManager.getConnection(connString, username, password)
        Log.w("Connection", "open")
    }

    fun executeSelect(query: String, function: KFunction2<Queries, ResultSet?, Unit>) : ResultSet? {
        try {
            try {
                val resultSet : ResultSet?

                while(conn.isClosed) connectToDB()

                val stmt: Statement = conn.createStatement()
                resultSet = stmt.executeQuery(query)
                function.invoke(this, resultSet)
                resultSet?.close()
                stmt.close()
                //conn.close()
                return resultSet
            } catch (e: Exception) {
                Log.w("Error connection", "" + e.message)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun executeInsertUpdate(query: String) : Int {
        try {
            while(conn.isClosed) connectToDB()
            val stmt: Statement = conn.createStatement()
            val res = stmt.executeUpdate(query)
            stmt.close()
            return res
        } catch(sqlE: SQLException) {
            sqlE.printStackTrace()
            if(sqlE.sqlState == "23000") return -1 //todo handle changename
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return 0
    }
    /*
        fun getStorageItems() {
            Constants.itemMap = mutableMapOf()
            myRef = DB_INSTANCE.getReference(STORAGE_ITEMS_DB_REFERENCE)
            myRef.orderByChild("name").addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    val item: StorageItem? = dataSnapshot.getValue(StorageItem::class.java)
                    try {
                        if (Constants.appIsStarting && checkStorageItem(item)) {
                            Constants.itemMap[item!!.name] = item
                        }
                    } catch (e : java.lang.Exception) {
                        Log.e(Constants.STORAGE_LOGGER, "Errore recupero item: \n"
                        + e.printStackTrace())
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }

            fun getStoricoItem() {
                Constants.historyItemList = mutableListOf()
                myRef = DB_INSTANCE.getReference("Storico")
                myRef.orderByChild("name").addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                        val item: StoricoItem? = dataSnapshot.getValue(StoricoItem::class.java)
                        deleteItem("" + item?.lastUpdate?.time, "Storico")
                    }
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {}
                })
            }

                fun getHistoryItems() {
                    Constants.historyItemList = mutableListOf()
                    myRef = DB_INSTANCE.getReference(HISTORY_ITEMS_DB_REFERENCE)
                    myRef.orderByChild("name").addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                            val item: HistoryItem? = dataSnapshot.getValue(HistoryItem::class.java)
                            if (Constants.appIsStarting && item?.lastUpdate?.compareTo(Date())!! <= 1) {
                                Constants.historyItemList.add(item)
                            }
                            if(ChronoUnit.HOURS.between(item?.lastUpdate?.toInstant(), Instant.now()) > 100)
                                deleteItem(item?.lastUpdate?.time.toString(), HISTORY_ITEMS_DB_REFERENCE)
                        }
                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                        override fun onChildRemoved(snapshot: DataSnapshot) {}
                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                        override fun onCancelled(error: DatabaseError) {}
                    })
                }


                fun <T: ItemInterface> addItem(item: T, reference: String) {
                    myRef = DB_INSTANCE.getReference(reference)
                    myRef.child(item.name).setValue(item)
                }

                fun <T: ItemInterface> updateItem(item: T, oldName: String, reference: String) {
                    myRef = DB_INSTANCE.getReference(reference)

                    if(oldName != item.name) deleteItem(oldName, reference)
                    addItem(item, reference)
                }

                fun deleteItem(itemName: String, reference: String) {
                    myRef = DB_INSTANCE.getReference(reference)
                    myRef.child(itemName).removeValue()
                }*/

    private fun checkStorageItem(storageItem: StorageItem?) : Boolean {
        if (storageItem == null || storageItem.name.isEmpty()
            || storageItem.section.name.isEmpty())
            return false
        return true
    }

    private fun selectQueryBuilder(tableName: String, whereConditions: MutableList<WhereCondition>) : String {
        val query : StringBuilder = java.lang.StringBuilder()
        query.append("SELECT * FROM ").append(tableName)

        if(whereConditions.isNotEmpty()) {
            query.append(" WHERE ")
            whereConditions.forEach { c -> query.append(c.buildCondition(whereConditions.indexOf(c) == whereConditions.size - 1)) }
        }
        Log.i(Constants.QUERY_LOGGER, "Query: $query")
        return query.toString()
    }

    private fun <T : ItemInterface> insertQueryBuilder(tableName: String, item: T) : String {
        val itemDTOList = getItemDTOListFromFields(item)
        val query : StringBuilder = java.lang.StringBuilder()
        val valuesSb = StringBuilder()
        val fieldsSb = StringBuilder()
        itemDTOList.forEach {
            val value = getValue(item, item.javaClass.getMethod("get${it.fieldName}"))
            if(!it.isNullable || value != "") {
                fieldsSb.append(it.columnName).append(",")
                valuesSb.append(getFormattedValue(value)).append(",")
            }
        }
        fieldsSb.deleteCharAt(fieldsSb.lastIndex)
        valuesSb.deleteCharAt(valuesSb.lastIndex)
        query.append("INSERT INTO ").append(tableName)
            .append(" (")
            .append(fieldsSb)
            .append(")")
            .append(" VALUES ").append("(")
            .append(valuesSb)
            .append(")")
        Log.i(Constants.QUERY_LOGGER, "Query: $query")
        return query.toString()
    }

    private fun <T: ItemInterface> getItemDTOListFromFields(item: T) : List<ItemDTO> {
        val fields = item.javaClass.fields.toMutableList()
        fields.addAll(item.javaClass.declaredFields)
        val fieldNames = fields.map { f -> f.name.replaceFirstChar { f.name.substring(0,1).uppercase() } }
        return fieldNames.map { f -> item.getItemDTOFromField(f) }.requireNoNulls()
    }

    private fun getFormattedValue(value: String) : String {
        val valueStr = StringBuilder()
        valueStr.append("'")
        valueStr.append(value)
        valueStr.append("'")

        return valueStr.toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun <T: ItemInterface> getValue(item: T, m: Method) : String {
        val res = m.invoke(item)
        var value = ""
        if(res == null || res == "null") value = ""
        else if(ItemInterface::class.javaObjectType.isAssignableFrom(res.javaClass)) value = (res as ItemInterface).name
        else if(Date::class.javaObjectType.isAssignableFrom(res.javaClass)) value = Utils.singleton.convertDateToString(res as Date, SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
        else value = res.toString()
        return value
    }

    private fun <T : ItemInterface> updateQueryBuilder(tableName: String, newItem: T, oldItem: T) : String {
        val query : StringBuilder = java.lang.StringBuilder()
        query.append("UPDATE ").append(tableName)
            .append(" SET ")

        val methods = newItem.javaClass.methods.asList()
        query.append(methods.map { m -> getMethodsByReflection(m, newItem, oldItem) }.joinToString { "," })
        query.append(" WHERE ").append(StorageItemColumnEnum.NAME).append(" = ").append(oldItem.name)
        Log.i(Constants.QUERY_LOGGER, "Query: $query")
        return query.toString()
    }

    private fun <T : ItemInterface> getMethodsByReflection(m: Method, newItem: T, oldItem: T) : String {
        val string = StringBuilder()
        val res = m.invoke(newItem)
        if(res != m.invoke(oldItem)) {
            string.append(" ").append(newItem.getItemDTOFromField(m.name)).append(" = ").append(res) //todo fix
        }
        return string.toString()
    }

}