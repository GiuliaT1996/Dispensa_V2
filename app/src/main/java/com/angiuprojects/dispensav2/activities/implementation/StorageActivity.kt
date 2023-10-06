package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.adapters.StorageUnitRecyclerAdapter
import com.angiuprojects.dispensav2.databinding.ActivityStorageBinding
import com.angiuprojects.dispensav2.databinding.HeaderLayoutBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils

class StorageActivity : BaseActivity<ActivityStorageBinding>(ActivityStorageBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, null, this,
            isFilterPresent = true,
            isSearchPresent = true
        )
        setStorageItemRecyclerAdapter(Constants.itemListFilteredByProfile)
    }

    override fun setSearchListener(header: HeaderLayoutBinding) {
        super.setSearchListener(header)

        header.searchInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(header.searchInput.editText.toString().isNotEmpty()) {
                    val filteredList = Utils.singleton.filterItemList(
                        Constants.itemListFilteredByProfile,
                        StorageItem::name,
                        header.searchInput.editText.toString()
                    )
                    setStorageItemRecyclerAdapter(filteredList)
                }
            }
        })
    }

    override fun setFilterListener(header: HeaderLayoutBinding) {
        super.setFilterListener(header)

        Utils.singleton.createNewDropDown(
            header.filterSpinner,
            this,
            R.drawable.spinner_background,
            SectionEnum.values().mapTo(mutableListOf()){it.formattedName}
        )
        //TODO TEST
    }

    private fun setStorageItemRecyclerAdapter(itemList: MutableList<StorageItem>) {
        val recyclerView = findViewById<RecyclerView>(R.id.storage_list)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = StorageUnitRecyclerAdapter(itemList, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }
}