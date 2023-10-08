package com.angiuprojects.dispensav2.enums

enum class ProfileEnum(val formattedName: String) {
    ANTONIO("Antonio"), GIULIA("Giulia"), COMUNI("Comuni");

    companion object {
        private val map = values().associateBy(ProfileEnum::formattedName)
        fun fromFormattedName(formattedName: String) = map[formattedName]
        fun getFormattedNames() = map
    }
}