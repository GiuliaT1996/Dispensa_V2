package com.angiuprojects.dispensav2.enums

enum class ProfileButtonStateEnum {
    ON, OFF;
    companion object {
        fun getProfileButtonStateEnum(state: String) :ProfileButtonStateEnum = if(state == "ON") ON else OFF
    }
}