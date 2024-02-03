package com.angiuprojects.dispensav2.utilities

import android.annotation.SuppressLint
import com.angiuprojects.dispensav2.entities.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class Constants {

    companion object {

        lateinit var singleton: Constants
        lateinit var historyItemList: MutableList<HistoryItem>
        lateinit var profileList: MutableList<Profile>
        lateinit var sectionList: MutableList<Section>
        lateinit var mealPlan: MealPlan
        lateinit var profileSettings: ProfileSettings
        lateinit var itemMap: MutableMap<String, StorageItem>

        const val DATE_FORMAT = "dd/MM/yyyy"
        @SuppressLint("SimpleDateFormat")
        val DATE_TIME_FORMATTER: DateFormat = SimpleDateFormat(DATE_FORMAT)
        const val STORAGE_LOGGER = "Storage Logger"
        const val QUERY_LOGGER = "Query Logger"

        var maxTicketInOneTransaction = 8

        fun initializeConstants() {
            singleton = Constants()
            historyItemList = mutableListOf()
            profileList = mutableListOf()
            sectionList = mutableListOf()
            mealPlan = MealPlan(sortedMapOf())
            profileSettings = ProfileSettings(mutableListOf())
            itemMap = mutableMapOf()
        }

    }
}