package com.angiuprojects.dispensav2.conversion

import java.util.*

class DispensaItem() {

    var name: String = ""
    var section: String = ""
    var subSection: String? = null
    var quantity: Int = 0
    var trigger: Int = 0
    var dataScadenza: Date? = null
    val deleted: Boolean? = null

    fun constructor(
        name: String,
        section: String,
        quantity: Int,
        trigger: Int,
        dataScadenza: Date?,
        subSection: String?
    ) {
        this.name = name
        this.section = section
        this.quantity = quantity
        this.trigger = trigger
        this.dataScadenza = dataScadenza
        this.subSection = subSection
    }

    override fun toString(): String {
        val s = StringBuilder()

        s.append("StorageItem: {\n")
            .append("Name: " + this.name + "\n")
            .append("Section: " + this.section + "\n")
            .append("Quantity: " + this.quantity + "\n")
            .append("Trigger: " + this.trigger + "\n")
            .append("Expiration Date: " + this.dataScadenza + "\n")

        return s.toString()
    }
}