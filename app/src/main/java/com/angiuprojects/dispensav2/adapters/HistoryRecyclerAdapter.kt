package com.angiuprojects.dispensav2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.dispensav2.databinding.HistoryUnitViewBinding
import com.angiuprojects.dispensav2.entities.HistoryItem
import com.angiuprojects.dispensav2.enums.HistoryActionEnum
import com.angiuprojects.dispensav2.utilities.Utils
import kotlin.math.abs

class HistoryRecyclerAdapter(private val dataSet: MutableList<HistoryItem>)
    : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>() {

    private lateinit var binding: HistoryUnitViewBinding

    class HistoryViewHolder(val binding: HistoryUnitViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.name
        var time: TextView = binding.time
        var action: TextView = binding.action

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        binding = HistoryUnitViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        with(dataSet[holder.adapterPosition]) {
            holder.name.text = this.storageName
            val lastLetter = if(abs(this.modifiedQuantity) == 1) "o" else "i"
            val addedRemoved = if(this.modifiedQuantity >= 0) "Aggiunt" else "Rimoss"
            holder.action.text = when (this.action) {
                HistoryActionEnum.INSERT -> String.format(this.action.message, lastLetter, abs(modifiedQuantity), lastLetter)
                HistoryActionEnum.UPDATE -> String.format(this.action.message, addedRemoved + lastLetter, abs(modifiedQuantity), lastLetter)
                HistoryActionEnum.DELETE -> this.action.message
            }

            val hoursSince = Utils.singleton.getHoursFromDate(this.lastUpdate)

            holder.time.text =
                if(hoursSince == 1L) String.format("%s ora fa.", hoursSince)
                else String.format("%s ore fa", hoursSince)
        }
    }

    override fun getItemCount(): Int { return dataSet.size }
}