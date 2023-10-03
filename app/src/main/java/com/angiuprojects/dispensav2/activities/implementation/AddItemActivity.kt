package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import android.widget.AutoCompleteTextView
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityAddItemBinding
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ProfileEnum
import com.angiuprojects.dispensav2.utilities.SectionEnum
import com.angiuprojects.dispensav2.utilities.Utils
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AddItemActivity : BaseActivity<ActivityAddItemBinding>(ActivityAddItemBinding::inflate) {

    // wasEmpty is used to handle the case we don't want to add an exp date
    private var wasEmpty: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackButtonListener(binding.header, null, this)

        Utils.getInstance().createNewDropDown(
            binding.sezioneAutoComplete,
            this,
            R.drawable.spinner_background,
            SectionEnum.values().mapTo(mutableListOf()){it.formattedName}
        )

        Utils.getInstance().createNewDropDown(
            binding.profileAutoComplete,
            this,
            R.drawable.spinner_background,
            ProfileEnum.values().mapTo(mutableListOf()){it.formattedName}
        )

        binding.createButton.setOnClickListener { onClickCreateItem() }
    }

    private fun onClickCreateItem() {
        wasEmpty = false

        if(getTextFromACTV(binding.sezioneAutoComplete) == null)
            Utils.getInstance().openSnackBar(snackBarView, "Inserire sezione!")
        else if(getTextFromInput(binding.name) == null || getTextFromInput(binding.quantity) == null
            || getTextFromInput(binding.trigger) == null)
            Utils.getInstance().openSnackBar(snackBarView, "Inserire tutti i campi obbligatori!")
        else if(getDate(binding.expDate) == null && !wasEmpty!!)
            Utils.getInstance().openSnackBar(snackBarView,
                "Il formato della data non è corretto! Inserire una data con il seguente formato: "
                        + Constants.DATE_FORMAT)
        else if(getTextFromACTV(binding.profileAutoComplete) == null)
            Utils.getInstance().openSnackBar(snackBarView, "Inserire profilo!")
        else {
            //TODO CREATE NEW ITEM
        }
    }

    private fun getTextFromInput(text: TextInputLayout): String? {
        return if (text.editText != null && text.editText!!.text != null && text.editText!!.text.toString()
                .isNotEmpty()
        ) text.editText!!.text.toString() else null
    }

    private fun getTextFromACTV(text: AutoCompleteTextView): String? {
        return if (text.text != null && text.text.toString().isNotEmpty() && text.text.toString() != "null"
        ) text.text.toString() else null
    }

    private fun getDate(dateStr: TextInputLayout): Date? {
        var date: Date? = null
        if (dateStr.editText != null && dateStr.editText!!.text != null && dateStr.editText!!.text.toString()
                .isNotEmpty()
        ) {
            date = Utils.getInstance().convertStringToDate(
                dateStr.editText!!.text.toString().trim { it <= ' ' })
        } else {
            wasEmpty = true
        }
        return date
    }

}