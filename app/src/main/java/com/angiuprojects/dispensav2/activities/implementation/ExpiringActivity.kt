package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.adapters.ExpiringRecyclerAdapter
import com.angiuprojects.dispensav2.databinding.ActivityExpiringBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.entities.WhereCondition
import com.angiuprojects.dispensav2.enums.ComparatorEnum
import com.angiuprojects.dispensav2.enums.ConditionEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils

class ExpiringActivity : BaseActivity<ActivityExpiringBinding>(ActivityExpiringBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, R.string.expiring_txt, this,
            isFilterPresent = false,
            isSearchPresent = false
        )

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.expiring_list),
            this,
            ExpiringRecyclerAdapter(filterExpiringItems())
        )
    }

    companion object {
        fun filterExpiringItems() : MutableList<StorageItem> {
            val whereConditions = mutableListOf<WhereCondition>()
            val c = WhereCondition("exp_date", "DATEADD(day, 7, GETDATE())", ComparatorEnum.LS, ConditionEnum.AND, false)
            whereConditions.add(c)
            Queries.singleton.selectItemsQuery(whereConditions)
            return Constants.itemMap.values.toMutableList()
        }
    }

}