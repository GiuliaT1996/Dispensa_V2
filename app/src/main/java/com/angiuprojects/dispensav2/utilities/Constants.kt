package com.angiuprojects.dispensav2.utilities

import android.widget.ImageButton

class Constants {

    companion object {

        private lateinit var singleton: Constants
        private lateinit var profileMap: MutableMap<ProfileEnum, ProfileButtonStateEnum>
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