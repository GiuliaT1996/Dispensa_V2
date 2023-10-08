package com.angiuprojects.dispensav2.entities

import com.angiuprojects.dispensav2.enums.HistoryActionEnum
import java.util.*

class HistoryItem() : ItemInterface() {
    var storageName: String = ""
    var lastUpdate: Date = Date()
    var action: HistoryActionEnum = HistoryActionEnum.INSERT
    var modifiedQuantity: Int = 0

    constructor(storageName: String, lastUpdate: Date, action:HistoryActionEnum, modifiedQuantity: Int) : this() {
        this.name = lastUpdate.time.toString()
        this.storageName = storageName
        this.action = action
        this.lastUpdate = lastUpdate
        this.modifiedQuantity = modifiedQuantity
    }

    override fun toString(): String {
        val s = StringBuilder()

        s.append("HistoryItem: {\n")
            .append("Name: " + this.name + "\n")
            .append("Storage Name: " + this.storageName + "\n")
            .append("Action: " + this.action.name + "\n")
            .append("last update: " + this.lastUpdate + "\n")
            .append("modified Quantity: " + this.modifiedQuantity + "\n")

        return s.toString()
    }

}