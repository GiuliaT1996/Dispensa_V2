package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.adapters.ShoppingListUnitRecyclerAdapter
import com.angiuprojects.dispensav2.databinding.ActivityShoppingListBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils

class ShoppingListActivity : BaseActivity<ActivityShoppingListBinding>(ActivityShoppingListBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, R.string.shopping_list_txt, this,
            isFilterPresent = false,
            isSearchPresent = false
        )

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.shopping_list),
            this,
            ShoppingListUnitRecyclerAdapter(filterShoppingList()))
    }

    private fun filterShoppingList() : MutableList<StorageItem> {
        //TODO add optional (trigger = -1)
        return Constants.itemListFilteredByProfile
            .filter { it.quantity <= it.trigger }
            .sortedBy { it.name }
            .toMutableList()
    }
}