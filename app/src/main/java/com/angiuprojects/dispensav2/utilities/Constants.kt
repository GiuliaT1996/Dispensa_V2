package com.angiuprojects.dispensav2.utilities

import android.annotation.SuppressLint
import com.angiuprojects.dispensav2.entities.ProfileSettings
import com.angiuprojects.dispensav2.entities.StorageItem
import java.text.DateFormat
import java.text.SimpleDateFormat

class Constants {

    companion object {

        lateinit var singleton: Constants
        lateinit var itemMap: MutableMap<String, StorageItem>
        lateinit var itemMapFilteredByProfile: MutableMap<String, StorageItem>

        lateinit var profileSettings: ProfileSettings

        const val DATE_FORMAT = "dd/MM/yyyy"
        @SuppressLint("SimpleDateFormat")
        val DATE_TIME_FORMATTER: DateFormat = SimpleDateFormat(DATE_FORMAT)
        const val STORAGE_LOGGER = "Storage Logger"

        var maxTicketInOneTransaction = 8

        fun initializeConstants() {
            singleton = Constants()
            itemMap = mutableMapOf()
            profileSettings = ProfileSettings()
            itemMapFilteredByProfile = mutableMapOf()
        }

    }
}