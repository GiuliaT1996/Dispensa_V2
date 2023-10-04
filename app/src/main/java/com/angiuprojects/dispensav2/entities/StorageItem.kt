package com.angiuprojects.dispensav2.entities

import java.time.Instant
import java.util.*

class StorageItem {

    var name: String = ""
    var section: String = ""
    var profile: String = ""
    var quantity: Int = 0
    var trigger: Int = 0
    var expirationDate: Date? = Date.from(Instant.now())

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
}