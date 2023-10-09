package com.angiuprojects.dispensav2.conversion

import android.util.Log
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.google.firebase.database.*

class OldQueries {

    companion object {

        lateinit var singleton : OldQueries

        fun initializeOldQueries() {
            singleton = OldQueries()
        }
    }

    private lateinit var myRef: DatabaseReference

    private val ITEM_DB_REFERENCE = "Items"

    fun getItems() {
        Constants.itemMap = mutableMapOf()

        ConvertUtils.singleton.dispensaItemList = mutableListOf()
        myRef = Queries.DB_INSTANCE.getReference(ITEM_DB_REFERENCE)
        myRef.orderByChild("name").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val item : DispensaItem? = dataSnapshot.getValue(DispensaItem::class.java)
                if (item != null && !item.deleted!!) {
                    val s = createStorageItemFromDispensaItem(item)
                    if(s != null ) {
                        Constants.itemMap[s.name] = s
                        Queries.singleton.addItem(s, Queries.STORAGE_ITEMS_DB_REFERENCE)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun createStorageItemFromDispensaItem(d: DispensaItem) : StorageItem? {

        Log.i(Constants.STORAGE_LOGGER, d.toString())

        val s = StorageItem()
        s.name = d.name.trim()
        if(!SectionEnum.getFormattedNames().containsKey(d.section.trim())) {
            Log.e(Constants.STORAGE_LOGGER, "PROBLEMA SEZIONE = " + d.name + " SEZIONE = " + d.section)
            return null
        }

        s.section = d.section.trim()

        when(d.section.trim()) {
            SectionEnum.CARNE.formattedName -> s.profile = ProfileEnum.COMUNI.formattedName
            SectionEnum.GATTI.formattedName -> s.profile = ProfileEnum.COMUNI.formattedName
            SectionEnum.IGIENE_PERSONALE.formattedName -> s.profile = ProfileEnum.COMUNI.formattedName
            SectionEnum.MERENDINE.formattedName -> s.profile = ProfileEnum.ANTONIO.formattedName
            SectionEnum.PESCE.formattedName -> s.profile = ProfileEnum.ANTONIO.formattedName
            SectionEnum.CONDIMENTI.formattedName -> s.profile = ProfileEnum.COMUNI.formattedName
            SectionEnum.PULIZIE.formattedName -> s.profile = ProfileEnum.COMUNI.formattedName
            SectionEnum.SNACK.formattedName -> s.profile = ProfileEnum.ANTONIO.formattedName
            else -> s.profile = ProfileEnum.GIULIA.formattedName
        }
        s.trigger = d.trigger
        s.quantity = d.quantity
        s.expirationDate = d.dataScadenza
        Log.i(Constants.STORAGE_LOGGER, "STORAGE ITEM CREATO: " + s)
        return s
    }

}