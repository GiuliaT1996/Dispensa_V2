package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityExpiringBinding

class ExpiringActivity : BaseActivity<ActivityExpiringBinding>(ActivityExpiringBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackButtonListener(binding.header, R.string.expiring_txt, this)
    }
}