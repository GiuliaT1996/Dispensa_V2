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
import com.angiuprojects.dispensav2.entities.Section
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.StorageItemUtils
import com.angiuprojects.dispensav2.utilities.Utils
import java.util.Locale

class StorageActivity : BaseActivity<ActivityStorageBinding>(ActivityStorageBinding::inflate) {

    private lateinit var optionalList: MutableList<StorageItem>
    private lateinit var mandatoryList: MutableList<StorageItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, null, this,
            isFilterPresent = true,
            isSearchPresent = true
        )

        optionalList = Utils.singleton.filterOptionalMandatoryList(isOptional = true, false, Constants.itemMap)
        mandatoryList = Utils.singleton.filterOptionalMandatoryList(isOptional = false, false, Constants.itemMap)

        binding.expandMandatory.setOnClickListener { Utils.singleton.expand(binding.expandMandatory, binding.storageListMandatory) }
        binding.expandOptional.setOnClickListener { Utils.singleton.expand(binding.expandOptional, binding.storageListOptional) }

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
            this,
            StorageRecyclerAdapter(mandatoryList, this))

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
            this,
            StorageRecyclerAdapter(optionalList, this))
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
                        val filteredOptList = optionalList.filter { it.name.lowercase()
                            .contains(s.toString().trim().lowercase()) }.toMutableList()
                        val filteredManList = mandatoryList.filter { it.name.lowercase()
                            .contains(s.toString().trim().lowercase()) }.toMutableList()
                        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
                            context,
                            StorageRecyclerAdapter(filteredManList, context))

                        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
                            context,
                            StorageRecyclerAdapter(filteredOptList, context))
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
            Constants.sectionList.map(Section::name)
                .toCollection(mutableListOf())
        )

        val context: Context = this
        
        header.filterSpinner.setOnItemClickListener { parent, _, position, _ ->
            run {
                val section = parent.getItemAtPosition(position) as String
                val filteredManList = mandatoryList.filter {
                    it.section.trim().lowercase(Locale.ROOT) == section.trim().lowercase(Locale.ROOT)
                }.toMutableList()
                val filteredOptList = optionalList.filter {
                    it.section.trim().lowercase(Locale.ROOT) == section.trim().lowercase(Locale.ROOT)
                }.toMutableList()
                Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
                    context,
                    StorageRecyclerAdapter(filteredManList, context))

                Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
                    context,
                    StorageRecyclerAdapter(filteredOptList, context))
            }
        }
    }

    override fun setCloseListener(header: HeaderLayoutBinding) {
        super.setCloseListener(header)

        header.filterSpinner.text = null
        header.searchInput.editText?.setText("")
        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_mandatory),
            this,
            StorageRecyclerAdapter(mandatoryList, this))

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list_optional),
            this,
            StorageRecyclerAdapter(optionalList, this))
    }
}