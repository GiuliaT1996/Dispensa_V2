package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityShoppingListBinding

class ShoppingListActivity : BaseActivity<ActivityShoppingListBinding>(ActivityShoppingListBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, R.string.shopping_list_txt, this,
            isFilterPresent = false,
            isSearchPresent = false
        )
    }
}