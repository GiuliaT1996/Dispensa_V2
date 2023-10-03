package com.angiuprojects.dispensav2.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.implementation.MainActivity
import com.angiuprojects.dispensav2.databinding.ActivityLoaderBinding
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.utilities.ProfileEnum
import com.angiuprojects.dispensav2.utilities.Utils

class LoaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoaderBinding
    private val duration: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.initializeUtilsSingleton()
        Constants.initializeConstantsSingleton()
        populateProfileMap()

        animateImage(this, binding.logoImage)
        animateText(binding.houseTxt)
        animateText(binding.presentsTxt)
        animateText(binding.storageTxt)
    }

    private fun animateImage(context: Context, imageView: ImageView) {
        val animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = duration
        imageView.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                Utils.getInstance().changeActivity(context, MainActivity::class.java)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun animateText(textView: TextView) {
        val animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = duration
        textView.startAnimation(animation)
    }

    private fun populateProfileMap() {
        //TODO: inserire stato profili recuperato da DB
        Constants.getInstance().getProfileMap()[ProfileEnum.ANTONIO] = ProfileButtonStateEnum.OFF
        Constants.getInstance().getProfileMap()[ProfileEnum.GIULIA] = ProfileButtonStateEnum.OFF
        Constants.getInstance().getProfileMap()[ProfileEnum.COMUNI] = ProfileButtonStateEnum.OFF
    }
}