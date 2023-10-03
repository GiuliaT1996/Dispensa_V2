package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityStorageBinding

class StorageActivity : BaseActivity<ActivityStorageBinding>(ActivityStorageBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackButtonListener(binding.header, R.string.storage_txt, this)
    }
}