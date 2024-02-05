package com.angiuprojects.dispensav2.activities.implementation

import android.app.Dialog
import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityAddItemBinding
import com.angiuprojects.dispensav2.entities.HistoryItem
import com.angiuprojects.dispensav2.entities.Profile
import com.angiuprojects.dispensav2.entities.Section
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.HistoryActionEnum
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.StorageItemUtils
import com.angiuprojects.dispensav2.utilities.Utils
import java.util.Date

class AddItemActivity : BaseActivity<ActivityAddItemBinding>(ActivityAddItemBinding::inflate) {

    private lateinit var dialog : Dialog


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
            Constants.sectionList.map(Section::name)
                .toCollection(mutableListOf())
        )

        Utils.singleton.createNewDropDown(
            binding.profileSpinner,
            this,
            R.drawable.spinner_background,
            Constants.profileList.map(Profile::name)
                .toCollection(mutableListOf())
        )

        binding.createButton.setOnClickListener { onClickCreateItem() }
    }

    private fun onClickCreateItem() {

        val storageItem = StorageItem()
        val getDateResponse = StorageItemUtils.singleton.getDate(binding.expDate, storageItem, StorageItem::expirationDate, false)

        if(StorageItemUtils.singleton.getTextFromACTV(binding.sectionSpinner, storageItem, StorageItem::section, Constants.sectionList) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire sezione!")
        else if(StorageItemUtils.singleton.getTextFromInputText(binding.name, storageItem, StorageItem::name, null) == null
            || StorageItemUtils.singleton.getTextFromInputInt(binding.quantity, storageItem, StorageItem::quantity, null) == null
            || StorageItemUtils.singleton.getTextFromInputInt(binding.trigger, storageItem, StorageItem::trigger, null) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire tutti i campi obbligatori!")
        else if(getDateResponse.first == null && !getDateResponse.second)
            Utils.singleton.openSnackBar(snackBarView,
                "Il formato della data non è corretto! Inserire una data con il seguente formato: "
                        + Constants.DATE_FORMAT)
        else if(StorageItemUtils.singleton.getTextFromACTV(binding.profileSpinner, storageItem, StorageItem::profile, Constants.profileList) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire profilo!")
        else {
            val historyItem = HistoryItem(storageItem.name, Date(), HistoryActionEnum.INSERT, storageItem.quantity)
            Constants.historyItemList.add(historyItem)
            //Queries.singleton.addItem(historyItem, Queries.HISTORY_ITEMS_DB_REFERENCE) //TODO
            val ins = StorageItemUtils.singleton.addStorageItem(storageItem)
            createPopUp(storageItem.name, ins)
        }
    }

    private fun onClickOk(dialog: Dialog?) {
        dialog?.dismiss()
        finish()
    }

    private fun onClickRetry(dialog: Dialog?) {
        dialog?.dismiss()
    }

    private fun createPopUp(name: String, insRes: Int) {
        dialog = Dialog(this)
        val message = if(insRes > 0) "L'oggetto %s è stato aggiunto alla dispensa."
        else if (insRes == -1) "Esiste già un oggetto che si chiama %s."
        else "Non è stato possibile aggiungere l'oggetto %s alla dispensa."
        Utils.singleton.createSimpleOKPopUp(
            String.format(message, name),
            dialog,
            if(insRes != -1) this::onClickOk else this::onClickRetry)
    }

}