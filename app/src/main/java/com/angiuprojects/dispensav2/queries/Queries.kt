package com.angiuprojects.dispensav2.queries

import android.util.Log
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.utilities.Constants
import com.google.firebase.database.*

class Queries {

    companion object {

        private val DB_INSTANCE: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://dispensa-v2-default-rtdb.firebaseio.com/")

        private val STORAGE_ITEMS_DB_REFERENCE = "StorageItems"
        private lateinit var myRef: DatabaseReference

        lateinit var singleton: Queries

        fun initializeQueries() {
            singleton = Queries()
        }

    }

    fun getItems() {
        Constants.itemList = mutableListOf()
        myRef = DB_INSTANCE.getReference(STORAGE_ITEMS_DB_REFERENCE)
        myRef.orderByChild("name").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val item: StorageItem? = dataSnapshot.getValue(StorageItem::class.java)
                if (checkItem(item)) {
                    Constants.itemList.add(item!!)
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

    fun checkItem(storageItem: StorageItem?) : Boolean {
        if (storageItem == null || storageItem.expirationDate == null || storageItem.name.isEmpty()
            || storageItem.section.isEmpty())
            return false
        return true
    }

}