package com.angiuprojects.dispensav2.entities

import com.angiuprojects.dispensav2.enums.StorageItemColumnEnum
import java.lang.reflect.Method
import java.util.*

class StorageItem() : ItemInterface() {

    var section: Section = Section()
    var profile: Profile = Profile()
    var quantity: Int = 0
    var trigger: Int = 0
    var expirationDate: Date? = null

    constructor(
        name: String,
        section: Section,
        profile: Profile,
        quantity: Int,
        trigger: Int,
        expirationDate: Date?) : this() {
        this.name = name
        this.section = section
        this.quantity = quantity
        this.profile = profile
        this.trigger = trigger
        this.expirationDate = expirationDate
    }

    override fun toString(): String {
        val s = StringBuilder()

        s.append("StorageItem: {\n")
            .append("Name: " + this.name + "\n")
            .append("Section: " + this.section.name + "\n")
            .append("Profile: " + this.profile + "\n")
            .append("Quantity: " + this.quantity + "\n")
            .append("Trigger: " + this.trigger + "\n")
            .append("Expiration Date: " + this.expirationDate + "\n")

        return s.toString()
    }

    override fun getFieldFromMethod(m: Method): String? {
        return when(m.name) {
            "name" -> StorageItemColumnEnum.NAME.columnName
            "section" -> StorageItemColumnEnum.SECTION.columnName
            "profile" -> StorageItemColumnEnum.PROFILE.columnName
            "quantity" -> StorageItemColumnEnum.QUANTITY.columnName
            "trigger" -> StorageItemColumnEnum.TRIGGER.columnName
            "expirationDate" -> StorageItemColumnEnum.EXP_DATE.columnName
            else -> null
        }
    }
}