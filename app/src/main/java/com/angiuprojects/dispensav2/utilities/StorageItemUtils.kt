package com.angiuprojects.dispensav2.utilities

import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import com.angiuprojects.dispensav2.entities.ItemInterface
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.entities.WhereCondition
import com.angiuprojects.dispensav2.enums.ComparatorEnum
import com.angiuprojects.dispensav2.enums.ConditionEnum
import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.exceptions.StorageException
import com.angiuprojects.dispensav2.queries.Queries
import com.google.android.material.textfield.TextInputLayout
import java.util.Date
import kotlin.reflect.KMutableProperty1

class StorageItemUtils {

    companion object {
        lateinit var singleton: StorageItemUtils

        fun initializeStorageItemUtils() {
            singleton = StorageItemUtils()
        }
    }

    fun addStorageItem(storageItem: StorageItem) : Int {
        return try {
            Queries.singleton.insertItem(storageItem)
        } catch (e : Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Non è stato possibile aggiungere l'elemento " + storageItem.name + ": " + e.message.toString())
            0
        }
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
                                     setterFunction: KMutableProperty1<StorageItem, String>, itemList: MutableList<String>?): String? {
        text as TextInputLayout
        if(itemList != null) return null
        return if (text.editText != null && text.editText!!.text != null && text.editText!!.text.toString()
                .isNotEmpty()) {
            val formattedText = text.editText!!.text.toString().trim().replace(".", "-")
                .replace("$", " ").replace("#", " ")
                .replace("[", " ").replace("]", " ")
                .replace("/", "-").replace("\\", "-")
            setterFunction.set(storageItem, formattedText)
            formattedText
        } else null
    }

    inline fun <reified T : ItemInterface> getTextFromACTV(
        text: View, storageItem: StorageItem,
        setterFunction: KMutableProperty1<StorageItem, T>, itemList: MutableList<T>?): String? {
        text as AutoCompleteTextView
        return if(text.text != null && text.text.toString().isNotEmpty() && text.text.toString() != "null") {
            if(!ItemInterface::class.javaObjectType.isAssignableFrom(T::class.java) || itemList == null)
                throw StorageException("The object must be an ItemInterface subclass!")
            val item = itemList.first { (it as ItemInterface).name == text.text.toString() }
            setterFunction.set(storageItem, item)
            text.text.toString().trim()
        } else null
    }

    fun getTextFromInputInt(text: View, storageItem: StorageItem,
                                    setterFunction: KMutableProperty1<StorageItem, Int>, itemList: MutableList<Int>?): String? {
        text as TextInputLayout
        if(itemList != null) return null
        return if (text.editText != null && text.editText!!.text != null && text.editText!!.text.toString()
                .isNotEmpty()) {
            try {
                setterFunction.set(storageItem, text.editText!!.text.toString().trim().toInt())
            } catch (e: Exception){
                Log.e(Constants.STORAGE_LOGGER, "Il formato di uno dei campi numerici non è corretto!")
                return null
            }
            text.editText!!.text.toString()
        } else null
    }

    fun filterByProfile() {
        val whereConditions = mutableListOf<WhereCondition>()
        Constants.profileSettings.profileList.forEach {
                p -> if(p.state == ProfileButtonStateEnum.ON) {
                    val c = WhereCondition("profile", "'${p.name}'", ComparatorEnum.EQ, ConditionEnum.OR, null)
                    whereConditions.add(c)
                }
        }
        if(whereConditions.isNotEmpty()) Queries.singleton.selectItemsQuery(whereConditions)
        else Constants.itemMap = mutableMapOf()
    }
}