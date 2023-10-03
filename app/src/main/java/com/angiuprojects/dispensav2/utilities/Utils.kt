package com.angiuprojects.dispensav2.utilities

import android.content.Context
import android.content.Intent
import android.widget.ImageButton

class Utils {

    companion object {

        private lateinit var singleton: Utils

        fun initializeUtilsSingleton(): Utils {
            singleton = Utils()
            return singleton
        }

        fun getInstance(): Utils {
            return singleton
        }
    }

    fun <T> changeActivity(context : Context, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        context.startActivity(intent)
    }
}