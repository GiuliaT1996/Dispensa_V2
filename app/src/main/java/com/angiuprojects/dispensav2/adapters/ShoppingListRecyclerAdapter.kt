package com.angiuprojects.dispensav2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.dispensav2.databinding.ShoppingListUnitViewBinding
import com.angiuprojects.dispensav2.entities.HistoryItem
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.HistoryActionEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import java.util.*

class ShoppingListRecyclerAdapter(private val dataSet: MutableList<StorageItem>)
    :RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingListUnitViewHolder>() {

    private lateinit var binding: ShoppingListUnitViewBinding
    private lateinit var addedQuantityList: MutableList<Int>
    private var isInit = true

    class ShoppingListUnitViewHolder(val binding: ShoppingListUnitViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.name
        var added: TextView = binding.added
        var alreadyPresent: TextView = binding.alreadyPresent
        var plusButton: ImageButton = binding.plusButton
        var minusButton: ImageButton = binding.minusButton
        var check: CheckBox = binding.check

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListUnitViewHolder {
        dataSet.sortBy { it.name }
        binding = ShoppingListUnitViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if(isInit) addedQuantityList = MutableList(dataSet.size) {0}
        isInit = false
        return ShoppingListUnitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListUnitViewHolder, position: Int) {
        with(dataSet[position]) {
            holder.name.text = this.name
            holder.alreadyPresent.text = String.format("P = %s", this.quantity)
            holder.added.text = String.format("C = %s", addedQuantityList[holder.adapterPosition])
            holder.plusButton.setOnClickListener { onClickPlusMinus(1, holder) }
            holder.minusButton.setOnClickListener { onClickPlusMinus(-1, holder) }
            holder.check.isChecked = false
            holder.check.setOnCheckedChangeListener { _, isChecked -> if (isChecked) updateQuantityField(this, holder) }
        }
    }

    private fun onClickPlusMinus(num: Int, holder: ShoppingListUnitViewHolder) {
        if(addedQuantityList[holder.adapterPosition] + num > 0) {
            addedQuantityList[holder.adapterPosition] = addedQuantityList[holder.adapterPosition] + num
            notifyItemChanged(holder.adapterPosition)
        }
    }

    private fun updateQuantityField(s: StorageItem, holder: ShoppingListUnitViewHolder) {
        val historyItem = HistoryItem(s.name, Date(), HistoryActionEnum.UPDATE, addedQuantityList[holder.adapterPosition])
        if(addedQuantityList[holder.adapterPosition] == 0) {
            holder.check.isChecked = false
            return
        }
        s.quantity = s.quantity + addedQuantityList[holder.adapterPosition]
        addedQuantityList[holder.adapterPosition] = 0
        Queries.singleton.updateItem(s, s.name, Queries.STORAGE_ITEMS_DB_REFERENCE)
        Constants.historyItemList.add(historyItem)
        Queries.singleton.addItem(historyItem, Queries.HISTORY_ITEMS_DB_REFERENCE)
        if(s.quantity > s.trigger) {
            dataSet.remove(s)
            addedQuantityList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            holder.itemView.visibility = View.GONE
            holder.itemView.visibility = View.VISIBLE
        }
        else notifyItemChanged(holder.adapterPosition)
    }

    override fun getItemCount(): Int { return dataSet.size }
}