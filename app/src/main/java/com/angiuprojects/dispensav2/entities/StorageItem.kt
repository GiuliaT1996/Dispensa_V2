package com.angiuprojects.dispensav2.entities

import java.util.*

class StorageItem {

    var name: String = ""
    var section: String = ""
    var profile: String = ""
    var quantity: Int = 0
    var trigger: Int = 0
    var expirationDate: Date? = null

    constructor()

    constructor(
        name: String,
        section: String,
        profile: String,
        quantity: Int,
        trigger: Int,
        expirationDate: Date?) {
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
            .append("Section: " + this.section + "\n")
            .append("Profile: " + this.profile + "\n")
            .append("Quantity: " + this.quantity + "\n")
            .append("Trigger: " + this.trigger + "\n")
            .append("Expiration Date: " + this.expirationDate + "\n")

        return s.toString()
    }
}