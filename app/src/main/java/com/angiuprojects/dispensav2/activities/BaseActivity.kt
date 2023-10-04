package com.angiuprojects.dispensav2.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.implementation.MainActivity
import com.angiuprojects.dispensav2.databinding.HeaderLayoutBinding
import com.angiuprojects.dispensav2.utilities.Utils

abstract class BaseActivity<B : ViewBinding> (val bindingFactory: (LayoutInflater) -> B) : AppCompatActivity() {

    lateinit var binding: B
    lateinit var snackBarView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        window.decorView.setBackgroundColor(resources.getColor(R.color.light_blue, baseContext.theme))
        snackBarView = findViewById(android.R.id.content)
    }

    open fun setBackButtonListener(header: HeaderLayoutBinding, textId: Int?, context: Context) {
        if (textId != null)
            header.activityName.setText(textId)
        header.backButton.setOnClickListener { Utils.singleton.changeActivity(context, MainActivity::class.java) }
    }
}