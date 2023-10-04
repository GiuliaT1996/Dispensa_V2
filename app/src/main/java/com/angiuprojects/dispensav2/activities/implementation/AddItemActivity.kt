package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityAddItemBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.utilities.StorageItemUtils
import com.angiuprojects.dispensav2.utilities.Utils
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.reflect.KMutableProperty1

class AddItemActivity : BaseActivity<ActivityAddItemBinding>(ActivityAddItemBinding::inflate) {

    // wasEmpty is used to handle the case we don't want to add an exp date
    private var wasEmpty: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackButtonListener(binding.header, null, this)

        Utils.singleton.createNewDropDown(
            binding.sezioneAutoComplete,
            this,
            R.drawable.spinner_background,
            SectionEnum.values().mapTo(mutableListOf()){it.formattedName}
        )

        Utils.singleton.createNewDropDown(
            binding.profileAutoComplete,
            this,
            R.drawable.spinner_background,
            ProfileEnum.values().mapTo(mutableListOf()){it.formattedName}
        )

        binding.createButton.setOnClickListener { onClickCreateItem() }
    }

    private fun onClickCreateItem() {
        wasEmpty = false

        val storageItem = StorageItem()

        if(getTextFromACTV(binding.sezioneAutoComplete, storageItem, StorageItem::section) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire sezione!")
        else if(getTextFromInputText(binding.name, storageItem, StorageItem::name) == null
            || getTextFromInputInt(binding.quantity, storageItem, StorageItem::quantity) == null
            || getTextFromInputInt(binding.trigger, storageItem, StorageItem::trigger) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire tutti i campi obbligatori!")
        else if(getDate(binding.expDate, storageItem, StorageItem::expirationDate) == null && !wasEmpty!!)
            Utils.singleton.openSnackBar(snackBarView,
                "Il formato della data non è corretto! Inserire una data con il seguente formato: "
                        + Constants.DATE_FORMAT)
        else if(getTextFromACTV(binding.profileAutoComplete, storageItem, StorageItem::profile) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire profilo!")
        else {
            //TODO SAVE STORAGE ITEM
            StorageItemUtils.singleton.addStorageItem(storageItem)
        }
    }

    // KMutableProperty1<StorageItem, String> -> SAME AS BICONSUMER IN JAVA - CAN BE USED AS GETTER AND SETTER
    private fun getTextFromInputText(text: TextInputLayout, storageItem: StorageItem,
                                 setterFunction: KMutableProperty1<StorageItem, String>): String? {
        return if (text.editText != null && text.editText!!.text != null && text.editText!!.text.toString()
                .isNotEmpty()) {
            setterFunction.set(storageItem, text.editText!!.text.toString())
            text.editText!!.text.toString()
        } else null
    }

    private fun getTextFromInputInt(text: TextInputLayout, storageItem: StorageItem,
                                 setterFunction: KMutableProperty1<StorageItem, Int>): String? {
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

    private fun getTextFromACTV(
        text: AutoCompleteTextView, storageItem: StorageItem,
        setterFunction: KMutableProperty1<StorageItem, String>): String? {
        return if(text.text != null && text.text.toString().isNotEmpty() && text.text.toString() != "null") {
            setterFunction.set(storageItem, text.text.toString())
            text.text.toString()
        } else null
    }

    private fun getDate(
        dateStr: TextInputLayout, storageItem: StorageItem,
        setterFunction: KMutableProperty1<StorageItem, Date?>
    ): Date? {
        var date: Date? = null
        if (dateStr.editText != null && dateStr.editText!!.text != null && dateStr.editText!!.text.toString()
                .isNotEmpty()
        ) {
            date = Utils.singleton.convertStringToDate(
                dateStr.editText!!.text.toString().trim { it <= ' ' })
            if (date != null) setterFunction.set(storageItem, date)
        } else {
            wasEmpty = true
        }
        return date
    }

}