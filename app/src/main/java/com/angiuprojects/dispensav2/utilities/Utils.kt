package com.angiuprojects.dispensav2.utilities

import android.R
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import java.util.*

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
    }

    fun createNewDropDown(
        dropdown: AutoCompleteTextView,
        context: Context,
        drawable: Int,
        suggestionList: MutableList<String>
    ) {
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, suggestionList)
        dropdown.setAdapter(adapter)
        dropdown.setDropDownBackgroundDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                drawable,
                context.theme
            )
        )
    }

    fun convertStringToDate(data: String?): Date? {
        return if (data != null) {
            try {
                Constants.DATE_TIME_FORMATTER.parse(data)
            } catch (e: Exception) {
                Log.e(Constants.STORAGE_LOGGER, "Il formato della data non è corretto")
                null
            }
        } else null
    }
}