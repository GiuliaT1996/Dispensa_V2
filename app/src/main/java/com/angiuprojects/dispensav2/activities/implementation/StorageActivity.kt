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
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.SectionEnum
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
import java.util.*

class StorageActivity : BaseActivity<ActivityStorageBinding>(ActivityStorageBinding::inflate) {

    private lateinit var itemList: MutableList<StorageItem>
    private var isOptional: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, null, this,
            isFilterPresent = true,
            isSearchPresent = true
        )
        binding.left.setOnClickListener { switchOptionalMandatory() }
        binding.right.setOnClickListener { switchOptionalMandatory() }

        val b = intent.extras
        if (b != null) isOptional = b.getBoolean("isOptional")

        binding.isOpt.text = if(isOptional) "OPZIONALE" else "IMPORTANTE"

        itemList = Utils.singleton.filterOptionalMandatoryList(isOptional = isOptional, false, Constants.itemMapFilteredByProfile)

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list),
            this,
            StorageRecyclerAdapter(itemList, this))
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
                        val filteredOptList = itemList.filter { it.name.lowercase()
                            .contains(s.toString().trim().lowercase()) }.toMutableList()
                        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list),
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
            SectionEnum.values().mapTo(mutableListOf()){it.formattedName}
        )

        val context: Context = this
        
        header.filterSpinner.setOnItemClickListener { parent, _, position, _ ->
            run {
                val section = parent.getItemAtPosition(position) as String
                val filteredManList = itemList.filter {
                    it.section.trim().lowercase(Locale.ROOT) == section.trim().lowercase(Locale.ROOT)
                }.toMutableList()
                Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list),
                    context,
                    StorageRecyclerAdapter(filteredManList, context))
            }
        }
    }

    override fun setCloseListener(header: HeaderLayoutBinding) {
        super.setCloseListener(header)

        header.filterSpinner.text = null
        header.searchInput.editText?.setText("")
        Utils.singleton.setRecyclerAdapter(findViewById(R.id.storage_list),
            this,
            StorageRecyclerAdapter(itemList, this))
    }

    private fun switchOptionalMandatory() {
        Utils.singleton.changeActivity(this, StorageActivity::class.java, !isOptional)
        finish()
    }
}