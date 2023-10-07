package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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

        binding.expandMandatory.setOnClickListener { expand(binding.expandMandatory, binding.shoppingListMandatory) }
        binding.expandOptional.setOnClickListener { expand(binding.expandOptional, binding.shoppingListOptional) }

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.shopping_list_mandatory),
            this,
            ShoppingListUnitRecyclerAdapter(filterShoppingList(isOptional = false)))

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.shopping_list_optional),
            this,
            ShoppingListUnitRecyclerAdapter(filterShoppingList(isOptional = true)))
    }

    private fun filterShoppingList(isOptional: Boolean) : MutableList<StorageItem> {
        return Constants.itemMapFilteredByProfile
            .filter { (isOptional && it.value.quantity == 0 && it.value.trigger < 0)
                    || (!isOptional && it.value.quantity <= it.value.trigger) }.values.toMutableList()
    }

    private fun expand(expandButton: ImageButton, recyclerView: RecyclerView) {
        if(recyclerView.visibility == View.VISIBLE) {
            expandButton.rotation = -90F
            recyclerView.visibility = View.GONE
        } else {
            expandButton.rotation = 0F
            recyclerView.visibility = View.VISIBLE
        }
    }
}