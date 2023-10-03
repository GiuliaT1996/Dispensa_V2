package com.angiuprojects.dispensav2.utilities

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import com.google.android.material.snackbar.Snackbar

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

    fun openSnackBar(snackBarView: View, text: String) {
        Snackbar.make(
            snackBarView, text,
            Snackbar.LENGTH_LONG
        ).show()
        return
    }
}