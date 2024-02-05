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

    override fun getItemDTOFromField(f: String): ItemDTO? {
        return when(f) {
            StorageItemColumnEnum.NAME.fieldName -> ItemDTO(StorageItemColumnEnum.NAME.fieldName, StorageItemColumnEnum.NAME.columnName, StorageItemColumnEnum.NAME.isNullable)
            StorageItemColumnEnum.SECTION.fieldName -> ItemDTO(StorageItemColumnEnum.SECTION.fieldName, StorageItemColumnEnum.SECTION.columnName, StorageItemColumnEnum.SECTION.isNullable)
            StorageItemColumnEnum.PROFILE.fieldName -> ItemDTO(StorageItemColumnEnum.PROFILE.fieldName, StorageItemColumnEnum.PROFILE.columnName, StorageItemColumnEnum.PROFILE.isNullable)
            StorageItemColumnEnum.QUANTITY.fieldName -> ItemDTO(StorageItemColumnEnum.QUANTITY.fieldName, StorageItemColumnEnum.QUANTITY.columnName, StorageItemColumnEnum.QUANTITY.isNullable)
            StorageItemColumnEnum.TRIGGER.fieldName -> ItemDTO(StorageItemColumnEnum.TRIGGER.fieldName, StorageItemColumnEnum.TRIGGER.columnName, StorageItemColumnEnum.TRIGGER.isNullable)
            StorageItemColumnEnum.EXP_DATE.fieldName -> ItemDTO(StorageItemColumnEnum.EXP_DATE.fieldName, StorageItemColumnEnum.EXP_DATE.columnName, StorageItemColumnEnum.EXP_DATE.isNullable)
            else -> null
        }
    }
}