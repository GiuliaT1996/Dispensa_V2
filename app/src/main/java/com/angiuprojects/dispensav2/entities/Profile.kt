package com.angiuprojects.dispensav2.entities

import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum

class Profile() : ItemInterface() {
    var state: ProfileButtonStateEnum = ProfileButtonStateEnum.OFF
    var image: String = ""

    constructor(name: String, state: ProfileButtonStateEnum, image: String) : this() {
        this.name = name
        this.state = state
        this.image = image
    }

    override fun getFieldList(): List<String> {
        return listOf("name", "profile_button_state", "profile_image")
    }
}