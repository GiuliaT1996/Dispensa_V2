package com.angiuprojects.dispensav2.utilities

import android.util.Log
import com.angiuprojects.dispensav2.entities.StorageItem

class StorageItemUtils {

    companion object {
        lateinit var singleton: StorageItemUtils

        fun initializeStorageItemUtils() {
            singleton = StorageItemUtils()
        }
    }

    fun addStorageItem(storageItem: StorageItem) : Boolean {
        try {
            Constants.itemList.add(storageItem)
        } catch (e : Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Non è stato possibile aggiungere l'elemento " + storageItem.name + ": " + e.message.toString())
            return false
        }
        return true
    }

    fun addStorageItemToIndex(storageItem: StorageItem, index: Int) : Boolean {
        try {
            Constants.itemList.add(index, storageItem)
        } catch (e : Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Non è stato possibile aggiungere l'elemento " + storageItem.name + ": " + e.message.toString())
            return false
        }
        return true
    }

    // use only when primary key is NOT changed
    fun updateStorageItem(storageItem: StorageItem) : Boolean {
        try {
            val oldStorageItem = Constants.itemList.filter{ s -> storageItem.name == s.name  }.toList()
            if(oldStorageItem.isEmpty()) {
                Log.e(Constants.STORAGE_LOGGER, "Non è stato trovato l'oggetto con nome " + storageItem.name)
                return false
            } else if(oldStorageItem.size > 1) {
                Log.e(Constants.STORAGE_LOGGER, "è stato trovato più di un oggetto con nome " + storageItem.name)
                return false
            }
            val index = Constants.itemList.indexOf(storageItem)
            Constants.itemList[index] = storageItem
        } catch (e: Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Non è stato possibile modificare l'elemento " + storageItem.name + ": " + e.message.toString())
            return false
        }
        return true
    }

    fun updateStorageItemAtIndex(storageItem: StorageItem, index: Int) : Boolean {
        try {
            Constants.itemList[index] = storageItem
        } catch (e: Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Non è stato possibile modificare l'elemento " + storageItem.name + ": " + e.message.toString())
            return false
        }
        return true
    }
}