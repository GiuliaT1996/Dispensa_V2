package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import android.util.Pair
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.adapters.HistoryRecyclerAdapter
import com.angiuprojects.dispensav2.databinding.ActivityHistoryBinding
import com.angiuprojects.dispensav2.entities.HistoryItem
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
import java.time.Instant
import java.time.temporal.ChronoUnit

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(ActivityHistoryBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, R.string.history_txt, this,
            isFilterPresent = false,
            isSearchPresent = false
        )

        Constants.historyItemList = merge(Constants.historyItemList)

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.history_list),
            this,
            HistoryRecyclerAdapter(Constants.historyItemList)
        )
    }

    private fun merge(hList: MutableList<HistoryItem>) : MutableList<HistoryItem> {
        val connected: MutableMap<String, Pair<HistoryItem, MutableList<HistoryItem>>> = mutableMapOf()
        val mergedList: MutableList<HistoryItem> = mutableListOf()

        hList.forEach { h1 ->
            val sameItems = hList.filter { h2 -> h2.storageName == h1.storageName && h2.action == h1.action
                    && ChronoUnit.HOURS.between(h1.lastUpdate.toInstant(), Instant.now()) ==
                    ChronoUnit.HOURS.between(h2.lastUpdate.toInstant(), Instant.now())
                    && ChronoUnit.HOURS.between(h1.lastUpdate.toInstant(), Instant.now()) < 25}.toMutableList()

            val key = h1.storageName + "/" + h1.action + "/" +
                    ChronoUnit.HOURS.between(h1.lastUpdate.toInstant(), Instant.now())

            if(!connected.containsKey(key))
                connected[key] = Pair(h1, sameItems)
        }
        connected.entries.forEach { entry ->
            val pair = entry.value
            var totalQuantity = 0
            pair.second.forEach { totalQuantity += it.modifiedQuantity }
            pair.first.modifiedQuantity = totalQuantity
            if(totalQuantity != 0)
                mergedList.add(pair.first)
        }
        return mergedList.sortedBy { it.lastUpdate }.reversed().toMutableList()
    }
}