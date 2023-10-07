package com.angiuprojects.dispensav2.utilities

import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.reflect.KMutableProperty1

class StorageItemUtils {

    companion object {
        lateinit var singleton: StorageItemUtils

        fun initializeStorageItemUtils() {
            singleton = StorageItemUtils()
        }
    }

    fun addStorageItem(storageItem: StorageItem) : Boolean {
        try {
            Constants.itemMap[storageItem.name] = storageItem
            if(Constants.profileSettings.profileMap[ProfileEnum.fromFormattedName(storageItem.profile)] == ProfileButtonStateEnum.ON)
                Constants.itemMapFilteredByProfile[storageItem.name] = storageItem
            Queries.singleton.addStorageItem(storageItem)
        } catch (e : Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Non è stato possibile aggiungere l'elemento " + storageItem.name + ": " + e.message.toString())
            return false
        }
        return true
    }

    fun getDate(dateStr: TextInputLayout, storageItem: StorageItem,
        setterFunction: KMutableProperty1<StorageItem, Date?>, isRemoveDate: Boolean
    ): Pair<Date?, Boolean> {
        // wasEmpty is used to handle the case we don't want to add an exp date
        var wasEmpty = false
        var date: Date? = null
        if (dateStr.editText != null && dateStr.editText!!.text != null && dateStr.editText!!.text.toString()
                .isNotEmpty() && !isRemoveDate
        ) {
            date = Utils.singleton.convertStringToDate(
                dateStr.editText!!.text.toString().trim { it <= ' ' })
            if (date != null) setterFunction.set(storageItem, date)
        } else {
            wasEmpty = true
        }
        return Pair(date, wasEmpty)
    }

    // KMutableProperty1<StorageItem, String> -> SAME AS BICONSUMER IN JAVA - CAN BE USED AS GETTER AND SETTER
    fun getTextFromInputText(text: View, storageItem: StorageItem,
                                     setterFunction: KMutableProperty1<StorageItem, String>): String? {
        text as TextInputLayout
        return if (text.editText != null && text.editText!!.text != null && text.editText!!.text.toString()
                .isNotEmpty()) {
            setterFunction.set(storageItem, text.editText!!.text.toString())
            text.editText!!.text.toString()
        } else null
    }

    fun getTextFromACTV(
        text: View, storageItem: StorageItem,
        setterFunction: KMutableProperty1<StorageItem, String>): String? {
        text as AutoCompleteTextView
        return if(text.text != null && text.text.toString().isNotEmpty() && text.text.toString() != "null") {
            setterFunction.set(storageItem, text.text.toString())
            text.text.toString()
        } else null
    }

    fun getTextFromInputInt(text: View, storageItem: StorageItem,
                                    setterFunction: KMutableProperty1<StorageItem, Int>): String? {
        text as TextInputLayout
        return if (text.editText != null && text.editText!!.text != null && text.editText!!.text.toString()
                .isNotEmpty()) {
            try {
                setterFunction.set(storageItem, text.editText!!.text.toString().toInt())
            } catch (e: Exception){
                Log.e(Constants.STORAGE_LOGGER, "Il formato di uno dei campi numerici non è corretto!")
                return null
            }
            text.editText!!.text.toString()
        } else null
    }
}