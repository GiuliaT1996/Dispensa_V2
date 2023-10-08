package com.angiuprojects.dispensav2.entities

import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.enums.ProfileEnum

class ProfileSettings() {

    var profileMap: MutableMap<ProfileEnum, ProfileButtonStateEnum> = mutableMapOf(
        Pair(ProfileEnum.ANTONIO, ProfileButtonStateEnum.OFF),
        Pair(ProfileEnum.GIULIA, ProfileButtonStateEnum.OFF),
        Pair(ProfileEnum.COMUNI, ProfileButtonStateEnum.OFF))

    override fun toString(): String {
        val s : StringBuilder = StringBuilder()
        s.append("ProfileSettings: {\n")
        profileMap.forEach { (profileEnum, profileButtonStateEnum) ->  s.append("   " + profileEnum.name + " -> " + profileButtonStateEnum.name + "; \n")}
        s.append("}")
        return s.toString()
    }
}