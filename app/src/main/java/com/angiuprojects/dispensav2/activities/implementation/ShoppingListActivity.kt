package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.adapters.ShoppingListRecyclerAdapter
import com.angiuprojects.dispensav2.databinding.ActivityShoppingListBinding
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils

class ShoppingListActivity : BaseActivity<ActivityShoppingListBinding>(ActivityShoppingListBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, R.string.shopping_list_txt, this,
            isFilterPresent = false,
            isSearchPresent = false
        )

        binding.expandMandatory.setOnClickListener { Utils.singleton.expand(binding.expandMandatory, binding.shoppingListMandatory) }
        binding.expandOptional.setOnClickListener { Utils.singleton.expand(binding.expandOptional, binding.shoppingListOptional) }

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.shopping_list_mandatory),
            this,
            ShoppingListRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = false, true, Constants.itemMapFilteredByProfile)))

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.shopping_list_optional),
            this,
            ShoppingListRecyclerAdapter(Utils.singleton.filterOptionalMandatoryList(isOptional = true, true, Constants.itemMapFilteredByProfile)))
    }
}