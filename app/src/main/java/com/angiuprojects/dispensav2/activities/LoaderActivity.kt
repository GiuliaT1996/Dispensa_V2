package com.angiuprojects.dispensav2.activities

import android.content.Context
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.angiuprojects.dispensav2.activities.implementation.MainActivity
import com.angiuprojects.dispensav2.databinding.ActivityLoaderBinding
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ReadWriteJsonUtils
import com.angiuprojects.dispensav2.utilities.StorageItemUtils
import com.angiuprojects.dispensav2.utilities.Utils

class LoaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoaderBinding
    private val duration: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSingletons()
        ReadWriteJsonUtils.singleton.getProfileSettings(this)

        animateImage(this, binding.logoImage)
        animateText(binding.houseTxt)
        animateText(binding.presentsTxt)
        animateText(binding.storageTxt)
    }

    private fun initializeSingletons() {
        Constants.initializeConstants()
        Queries.initializeQueries()
        Utils.initializeUtils()
        ReadWriteJsonUtils.initializeReadWriteJsonUtils()
        StorageItemUtils.initializeStorageItemUtils()
    }

    private fun animateImage(context: Context, imageView: ImageView) {
        val animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = duration
        imageView.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                Utils.singleton.changeActivity(context, MainActivity::class.java)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun animateText(textView: TextView) {
        val animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = duration
        textView.startAnimation(animation)
    }
}