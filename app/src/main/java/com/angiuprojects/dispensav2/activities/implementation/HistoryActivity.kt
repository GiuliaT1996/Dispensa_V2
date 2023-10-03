package com.angiuprojects.dispensav2.activities.implementation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityHistoryBinding

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(ActivityHistoryBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackButtonListener(binding.header, R.string.history_txt, this)
    }
}