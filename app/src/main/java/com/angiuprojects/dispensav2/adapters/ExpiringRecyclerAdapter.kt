package com.angiuprojects.dispensav2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.angiuprojects.dispensav2.databinding.ExpiringUnitViewBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.utilities.Utils

class ExpiringRecyclerAdapter(private val dataSet: MutableList<StorageItem>)
    : Adapter<ExpiringRecyclerAdapter.ExpiringUnitViewHolder>() {

    private lateinit var binding: ExpiringUnitViewBinding

    class ExpiringUnitViewHolder(val binding: ExpiringUnitViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.name
        var expDate: TextView = binding.expDate
        var missingDays: TextView = binding.missingDays

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiringUnitViewHolder {
        dataSet.sortBy { it.name }
        binding = ExpiringUnitViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpiringUnitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpiringUnitViewHolder, position: Int) {
        with(dataSet[holder.adapterPosition]) {
            holder.name.text = this.name
            holder.expDate.text = Utils.singleton.convertDateToString(this.expirationDate)
            holder.missingDays.text = this.expirationDate?.let { Utils.singleton.setPhrase(it) }
        }
    }

    override fun getItemCount(): Int { return dataSet.size }
}