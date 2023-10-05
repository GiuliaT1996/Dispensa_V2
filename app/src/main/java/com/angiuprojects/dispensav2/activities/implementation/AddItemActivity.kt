package com.angiuprojects.dispensav2.activities.implementation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityAddItemBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
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

        setHeaderListener(binding.header, null, this,
            isFilterPresent = false,
            isSearchPresent = false
        )

        Utils.singleton.createNewDropDown(
            binding.sectionSpinner,
            this,
            R.drawable.spinner_background,
            SectionEnum.values().mapTo(mutableListOf()){it.formattedName}
        )

        Utils.singleton.createNewDropDown(
            binding.profileSpinner,
            this,
            R.drawable.spinner_background,
            ProfileEnum.values().mapTo(mutableListOf()){it.formattedName}
        )

        binding.createButton.setOnClickListener { onClickCreateItem() }
    }

    private fun onClickCreateItem() {
        wasEmpty = false

        val storageItem = StorageItem()

        if(getTextFromACTV(binding.sectionSpinner, storageItem, StorageItem::section) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire sezione!")
        else if(getTextFromInputText(binding.name, storageItem, StorageItem::name) == null
            || getTextFromInputInt(binding.quantity, storageItem, StorageItem::quantity) == null
            || getTextFromInputInt(binding.trigger, storageItem, StorageItem::trigger) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire tutti i campi obbligatori!")
        else if(getDate(binding.expDate, storageItem, StorageItem::expirationDate) == null && !wasEmpty!!)
            Utils.singleton.openSnackBar(snackBarView,
                "Il formato della data non è corretto! Inserire una data con il seguente formato: "
                        + Constants.DATE_FORMAT)
        else if(getTextFromACTV(binding.profileSpinner, storageItem, StorageItem::profile) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire profilo!")
        else {
            StorageItemUtils.singleton.addStorageItem(storageItem)
            createPopUp(storageItem.name)
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

    private fun onClickOk() {
        Queries.singleton.getItems()
        finish()
    }

    private fun createPopUp(name: String) {
        val dialog = Dialog(this)
        val popUpView: View = layoutInflater.inflate(R.layout.pop_up_simple_ok, null)
        dialog.setContentView(popUpView)
        val confirmationText = popUpView.findViewById<TextView>(R.id.text)
        val okButton = popUpView.findViewById<Button>(R.id.ok_button)
        confirmationText.text =
            String.format("L'oggetto %s è stato aggiunto alla dispensa.", name)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        okButton.setOnClickListener { onClickOk() }
    }

}