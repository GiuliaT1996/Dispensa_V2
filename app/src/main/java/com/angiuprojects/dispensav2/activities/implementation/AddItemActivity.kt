package com.angiuprojects.dispensav2.activities.implementation

import android.app.Dialog
import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityAddItemBinding
import com.angiuprojects.dispensav2.entities.HistoryItem
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.HistoryActionEnum
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.StorageItemUtils
import com.angiuprojects.dispensav2.utilities.Utils
import java.util.*

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

        val storageItem = StorageItem()
        val getDateResponse = StorageItemUtils.singleton.getDate(binding.expDate, storageItem, StorageItem::expirationDate, false)

        if(StorageItemUtils.singleton.getTextFromACTV(binding.sectionSpinner, storageItem, StorageItem::section) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire sezione!")
        else if(StorageItemUtils.singleton.getTextFromInputText(binding.name, storageItem, StorageItem::name) == null
            || StorageItemUtils.singleton.getTextFromInputInt(binding.quantity, storageItem, StorageItem::quantity) == null
            || StorageItemUtils.singleton.getTextFromInputInt(binding.trigger, storageItem, StorageItem::trigger) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire tutti i campi obbligatori!")
        else if(getDateResponse.first == null && !getDateResponse.second)
            Utils.singleton.openSnackBar(snackBarView,
                "Il formato della data non è corretto! Inserire una data con il seguente formato: "
                        + Constants.DATE_FORMAT)
        else if(StorageItemUtils.singleton.getTextFromACTV(binding.profileSpinner, storageItem, StorageItem::profile) == null)
            Utils.singleton.openSnackBar(snackBarView, "Inserire profilo!")
        else if(Constants.itemMap.containsKey(storageItem.name)) {
            Utils.singleton.openSnackBar(snackBarView,
                "Esiste già un oggetto con nome " + storageItem.name)
        }
        else {
            val historyItem = HistoryItem(storageItem.name, Date(), HistoryActionEnum.INSERT, storageItem.quantity)
            Constants.historyItemList.add(historyItem)
            Queries.singleton.addItem(historyItem, Queries.HISTORY_ITEMS_DB_REFERENCE)
            StorageItemUtils.singleton.addStorageItem(storageItem)
            createPopUp(storageItem.name)
        }
    }

    private fun onClickOk(dialog: Dialog?) {
        dialog?.dismiss()
        finish()
    }

    private fun createPopUp(name: String) {
        dialog = Dialog(this)
        Utils.singleton.createSimpleOKPopUp(
            String.format("L'oggetto %s è stato aggiunto alla dispensa.", name),
            dialog,
            this::onClickOk)
    }

}