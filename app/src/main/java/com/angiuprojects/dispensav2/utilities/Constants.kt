package com.angiuprojects.dispensav2.utilities

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat

class Constants {

    companion object {

        private lateinit var singleton: Constants
        private lateinit var profileMap: MutableMap<ProfileEnum, ProfileButtonStateEnum>

        const val DATE_FORMAT = "dd/MM/yyyy"
        @SuppressLint("SimpleDateFormat")
        val DATE_TIME_FORMATTER: DateFormat = SimpleDateFormat(DATE_FORMAT)
        const val STORAGE_LOGGER = "Storage Logger"

        var maxTicketInOneTransaction = 8

        fun initializeConstantsSingleton(): Constants {
            singleton = Constants()
            profileMap = mutableMapOf()
            return singleton
        }

        fun getInstance(): Constants {
            return singleton
        }
    }

    fun getProfileMap(): MutableMap<ProfileEnum, ProfileButtonStateEnum> {
        return profileMap
    }



}