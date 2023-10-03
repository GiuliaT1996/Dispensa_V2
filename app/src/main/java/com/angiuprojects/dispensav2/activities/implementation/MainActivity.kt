package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityMainBinding
import com.angiuprojects.dispensav2.utilities.Utils

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: SPOSTARE NELL LOADER
        Utils.initializeUtilsSingleton()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.storageButton.setOnClickListener { onClickOpenStorage() }
        binding.shoppingListButton.setOnClickListener { onClickOpenShoppingList() }
        binding.expiringButton.setOnClickListener { onClickOpenExpiring() }
        binding.addItemButton.setOnClickListener { onClickOpenAddItem() }
        binding.calculatorButton.setOnClickListener { onClickOpenTicketCalculator() }
        binding.historyButton.setOnClickListener { onClickOpenHistory() }
    }

    private fun onClickOpenStorage() {
        Utils.getInstance().changeActivity(this, StorageActivity::class.java)
    }

    private fun onClickOpenShoppingList() {
        Utils.getInstance().changeActivity(this, ShoppingListActivity::class.java)
    }

    private fun onClickOpenExpiring() {
        Utils.getInstance().changeActivity(this, ExpiringActivity::class.java)
    }

    private fun onClickOpenAddItem() {
        Utils.getInstance().changeActivity(this, AddItemActivity::class.java)
    }

    private fun onClickOpenTicketCalculator() {
        Utils.getInstance().changeActivity(this, TicketCalculatorActivity::class.java)
    }

    private fun onClickOpenHistory() {
        Utils.getInstance().changeActivity(this, HistoryActivity::class.java)
    }
}