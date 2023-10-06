package com.angiuprojects.dispensav2.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.dispensav2.databinding.StorageUnitViewBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils

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
            holder.editButton.setOnClickListener { editButton(this) }
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

    private fun editButton(s: StorageItem) {

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
        Constants.itemList.remove(s)
        Queries.singleton.deleteStorageItem(s.name)
        this.notifyItemRemoved(holder.adapterPosition)
        dialog.dismiss()
    }

    override fun getItemCount(): Int { return dataSet.size }
}