package com.angiuprojects.dispensav2.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.databinding.PopUpModifyStorageItemBinding
import com.angiuprojects.dispensav2.databinding.StorageUnitViewBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.StorageItemUtils
import com.angiuprojects.dispensav2.utilities.Utils
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KFunction4

class StorageUnitRecyclerAdapter(private val dataSet: MutableList<StorageItem>, private val context: Context)
    : RecyclerView.Adapter<StorageUnitRecyclerAdapter.StorageUnitViewHolder>(){

    private lateinit var binding: StorageUnitViewBinding
    private lateinit var dialog: Dialog

    class StorageUnitViewHolder(val binding: StorageUnitViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.name
        var section: TextView = binding.section
        var quantity: TextView = binding.quantity
        var expirationDate: TextView = binding.expDate
        var plusButton: ImageButton = binding.plusButton
        var minusButton: ImageButton = binding.minusButton
        var editButton: ImageButton = binding.editButton
        var deleteButton: ImageButton = binding.deleteButton

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageUnitViewHolder {
        binding = StorageUnitViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StorageUnitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StorageUnitViewHolder, position: Int) {
        with(dataSet[position]) {
            holder.name.text = name
            holder.section.text = section
            holder.expirationDate.text = Utils.singleton.convertDateToString(expirationDate)
            holder.quantity.text = quantity.toString()
            holder.plusButton.setOnClickListener { onClickPlusMinus(1, this, holder) }
            holder.minusButton.setOnClickListener { onClickPlusMinus(-1, this, holder) }
            holder.editButton.setOnClickListener { editButton(this, holder) }
            holder.deleteButton.setOnClickListener { deleteButton(this, holder) }
        }
    }

    private fun onClickPlusMinus(num: Int, s: StorageItem, holder: StorageUnitViewHolder) {
        if(s.quantity + num > 0 || (s.quantity + num == 0 && s.expirationDate == null))
            updateQuantity(s, num, holder, false)
        else if (s.quantity + num == 0)
            openExpDatePopUp(s, num, holder)
    }

    private fun openExpDatePopUp(s: StorageItem, num: Int, holder: StorageUnitViewHolder) {
        dialog = Dialog(context)
        Utils.singleton.createYesNoPopUp(
            "Eliminare la data di scadenza?",
            dialog,
            StorageUnitRecyclerAdapter::onClickDeleteExpDate,
            this,
            s,
            num,
            holder)
    }

    private fun onClickDeleteExpDate(s: StorageItem, num: Int, holder: StorageUnitViewHolder, isYes: Boolean) {
        updateQuantity(s, num, holder, isYes)
        dialog.dismiss()
    }
    private fun updateQuantity(
        s: StorageItem,
        num: Int,
        holder: StorageUnitViewHolder,
        deleteExpDate: Boolean
    ) {
        if (deleteExpDate) s.expirationDate = null
        s.quantity = s.quantity + num
        //TODO STORICO
        Queries.singleton.updateStorageItem(s, s.name)
        this.notifyItemChanged(holder.adapterPosition)
    }

    private fun editButton(s: StorageItem, holder: StorageUnitViewHolder) {
        dialog = Dialog(context)
        val binding = Utils.singleton.getPopUpBinding(dialog, PopUpModifyStorageItemBinding::inflate)

        createDropdowns(binding)
        setHintInformations(binding, s)

        binding?.editButton?.setOnClickListener { onClickEdit(binding, s, holder) }
        binding?.closeButton?.setOnClickListener { dialog.dismiss() }
    }

    private fun onClickEdit(binding: PopUpModifyStorageItemBinding, oldStorageItem: StorageItem, holder: StorageUnitViewHolder) {
        val newStorageItem = StorageItem()

        val snackBarView = dialog.window!!.findViewById<View>(android.R.id.content)
        val getDateResponse = StorageItemUtils.singleton.getDate(binding.expDate, newStorageItem,
            StorageItem::expirationDate, binding.expDateSwitch.isChecked)

        if(getDateResponse.first == null && !getDateResponse.second) {
            Utils.singleton.openSnackBar(snackBarView,
                "Il formato della data non è corretto! Inserire una data con il seguente formato: "
                        + Constants.DATE_FORMAT)
        } else {
            checkIfNewIsPopulated(binding.sectionSpinner, newStorageItem, oldStorageItem, StorageItem::section,
                StorageItemUtils::getTextFromACTV, StorageItemUtils.singleton)
            checkIfNewIsPopulated(binding.name, newStorageItem, oldStorageItem, StorageItem::name,
                StorageItemUtils::getTextFromInputText, StorageItemUtils.singleton)
            checkIfNewIsPopulated(binding.profileSpinner, newStorageItem, oldStorageItem, StorageItem::profile,
                StorageItemUtils::getTextFromACTV, StorageItemUtils.singleton)
            checkIfNewIsPopulated(binding.quantity, newStorageItem, oldStorageItem, StorageItem::quantity,
                StorageItemUtils::getTextFromInputInt, StorageItemUtils.singleton)
            checkIfNewIsPopulated(binding.trigger, newStorageItem, oldStorageItem, StorageItem::trigger,
                StorageItemUtils::getTextFromInputInt, StorageItemUtils.singleton)

            if(newStorageItem.expirationDate == null && !binding.expDateSwitch.isChecked)
                newStorageItem.expirationDate = oldStorageItem.expirationDate

            //TODO STORICO
            Constants.itemList[Constants.itemList.indexOf(oldStorageItem)] = newStorageItem
            Constants.itemListFilteredByProfile[Constants.itemListFilteredByProfile.indexOf(oldStorageItem)] = newStorageItem
            Queries.singleton.updateStorageItem(newStorageItem, oldStorageItem.name)
            dataSet[holder.adapterPosition] = newStorageItem
            this.notifyItemChanged(holder.adapterPosition)
            dialog.dismiss()
        }

    }

    private fun <T> checkIfNewIsPopulated(
        view: View, newStorageItem: StorageItem,
        oldStorageItem: StorageItem,
        getSetFieldFunction: KMutableProperty1<StorageItem, T>,
        getResultStringFunction: KFunction4<StorageItemUtils, View, StorageItem, KMutableProperty1<StorageItem, T>, String?>,
        invoker: StorageItemUtils
    ) {
        if(getResultStringFunction.invoke(invoker, view, newStorageItem, getSetFieldFunction) == null)
            getSetFieldFunction.set(newStorageItem, getSetFieldFunction.get(oldStorageItem))
    }

    private fun createDropdowns(binding: PopUpModifyStorageItemBinding?) {
        binding?.let { _ ->
            Utils.singleton.createNewDropDown(
                binding.sectionSpinner,
                context,
                R.drawable.spinner_background,
                SectionEnum.values().mapTo(mutableListOf()){it.formattedName}
            )
            Utils.singleton.createNewDropDown(
                binding.profileSpinner,
                context,
                R.drawable.spinner_background,
                ProfileEnum.values().mapTo(mutableListOf()){it.formattedName}
            )
        }
    }

    private fun setHintInformations(binding: PopUpModifyStorageItemBinding?, s: StorageItem) {
        binding?.name?.hint = String.format("Nome: %s",  s.name)
        binding?.trigger?.hint = String.format("Trigger: %s",  s.trigger)
        binding?.quantity?.hint = String.format("Quantità: %s",  s.quantity)
        binding?.expDate?.hint = String.format("Data Scadenza: %s",  Utils.singleton.convertDateToString(s.expirationDate))

        binding?.expDateSwitch?.setOnCheckedChangeListener { _, isChecked -> binding.expDate.isEnabled = isChecked }
        binding?.sectionSpinner?.setText(s.section)
        binding?.profileSpinner?.setText(s.profile)
    }

    private fun deleteButton(s: StorageItem, holder: StorageUnitViewHolder) {
        dialog = Dialog(context)
        Utils.singleton.createYesNoPopUp(
            String.format("Sei sicuro di voler eliminare %s?", s.name),
            dialog,
            StorageUnitRecyclerAdapter::onClickDelete,
            this,
            s,
            holder)
    }

    private fun onClickDelete(s: StorageItem, holder: StorageUnitViewHolder) {
        //TODO STORICO
        dataSet.remove(s)
        Constants.itemList.remove(s)
        Constants.itemListFilteredByProfile.remove(s)
        Queries.singleton.deleteStorageItem(s.name)
        this.notifyItemRemoved(holder.adapterPosition)
        dialog.dismiss()
    }

    override fun getItemCount(): Int { return dataSet.size }
}