package com.angiuprojects.dispensav2.activities.implementation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.adapters.StorageRecyclerAdapter
import com.angiuprojects.dispensav2.databinding.ActivityStorageBinding
import com.angiuprojects.dispensav2.databinding.HeaderLayoutBinding
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
import java.util.Locale

class StorageActivity : BaseActivity<ActivityStorageBinding>(ActivityStorageBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, null, this,
            isFilterPresent = true,
            isSearchPresent = true
        )

        binding.expandMandatory.setOnClickListener { Utils.singleton.expand(binding.expandMandatory, binding.storageListMandatory) }
        binding.expandOptional.setOnClickListener { Utils.singleton.expand(binding.expandOptional, binding.storageListOptional) }

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
            this,
            StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = false, Constants.itemMapFilteredByProfile), this))

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
            this,
            StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = true, Constants.itemMapFilteredByProfile), this))
    }

    override fun setSearchListener(header: HeaderLayoutBinding) {
        super.setSearchListener(header)

        val context : Context = this

        header.searchInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(header.searchInput.editText.toString().isNotEmpty()) {
                    if(s != null) {
                        val filteredMap = Constants.itemMapFilteredByProfile.filter { it.key.lowercase()
                            .contains(s.toString().trim().lowercase()) }.toMutableMap()
                        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
                            context,
                            StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = false, filteredMap), context))

                        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
                            context,
                            StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = true, filteredMap), context))
                    }
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

        val context: Context = this
        
        header.filterSpinner.setOnItemClickListener { parent, _, position, _ ->
            run {
                val section = parent.getItemAtPosition(position) as String
                val filteredMap = Constants.itemMapFilteredByProfile.filter {
                    it.value.section.trim().lowercase(Locale.ROOT) == section.trim().lowercase(Locale.ROOT)
                }.toMutableMap()
                Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
                    context,
                    StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = false, filteredMap), context))

                Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
                    context,
                    StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = true, filteredMap), context))
            }
        }
    }

    override fun setCloseListener(header: HeaderLayoutBinding) {
        super.setCloseListener(header)

        header.filterSpinner.text = null
        header.searchInput.editText?.setText("")
        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
            this,
            StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = false, Constants.itemMapFilteredByProfile), this))

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
            this,
            StorageRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = true, Constants.itemMapFilteredByProfile), this))
    }
}