package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.adapters.ExpiringUnitRecyclerAdapter
import com.angiuprojects.dispensav2.databinding.ActivityExpiringBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class ExpiringActivity : BaseActivity<ActivityExpiringBinding>(ActivityExpiringBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHeaderListener(binding.header, R.string.expiring_txt, this,
            isFilterPresent = false,
            isSearchPresent = false
        )

        Utils.singleton.setRecyclerAdapter(findViewById(R.id.expiring_list),
            this,
            ExpiringUnitRecyclerAdapter(filterExpiringItems())
        )
    }

    companion object {
        fun filterExpiringItems() : MutableList<StorageItem> {
            return Constants.itemMap.filter { it.value.expirationDate != null && it.value.expirationDate!! <
                    Date.from(Instant.now().plus(7, ChronoUnit.DAYS)) }.values.toMutableList()
        }
    }

}