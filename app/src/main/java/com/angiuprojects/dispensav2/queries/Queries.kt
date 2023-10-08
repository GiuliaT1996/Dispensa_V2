package com.angiuprojects.dispensav2.queries

import android.util.Log
import com.angiuprojects.dispensav2.entities.HistoryItem
import com.angiuprojects.dispensav2.entities.ItemInterface
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
import com.google.firebase.database.*
import java.util.*

class Queries {

    companion object {

        private val DB_INSTANCE: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://dispensa-v2-default-rtdb.firebaseio.com/")

        const val STORAGE_ITEMS_DB_REFERENCE = "StorageItems"
        const val HISTORY_ITEMS_DB_REFERENCE = "HistoryItems"
        private lateinit var myRef: DatabaseReference

        lateinit var singleton: Queries

        fun initializeQueries() {
            singleton = Queries()
        }
    }

    fun getStorageItems() {
        Constants.itemMap = mutableMapOf()
        myRef = DB_INSTANCE.getReference(STORAGE_ITEMS_DB_REFERENCE)
        myRef.orderByChild("name").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val item: StorageItem? = dataSnapshot.getValue(StorageItem::class.java)
                if (Constants.appIsStarting && checkStorageItem(item)) {
                    Constants.itemMap[item!!.name] = item
                    Log.i(Constants.STORAGE_LOGGER,
                        "Name : " + dataSnapshot.key + " - Section : " + item.section)
                }
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
                    Log.i(Constants.STORAGE_LOGGER,
                        "Name : " + dataSnapshot.key + " - Action : " + item.action)
                }
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

        if(oldName != item.name) deleteStorageItem(oldName, reference)
        addItem(item, reference)
    }

    fun deleteStorageItem(itemName: String, reference: String) {
        myRef = DB_INSTANCE.getReference(reference)
        myRef.child(itemName).removeValue()
    }

    private fun checkStorageItem(storageItem: StorageItem?) : Boolean {
        if (storageItem == null || storageItem.name.isEmpty()
            || storageItem.section.isEmpty())
            return false
        return true
    }

}