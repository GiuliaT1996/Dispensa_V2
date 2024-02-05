package com.angiuprojects.dispensav2.entities

open class ItemInterface() {
    lateinit var name: String

    constructor(name: String) : this() {
        this.name = name
    }

    open fun getItemDTOFromField(f: String) : ItemDTO? {
        return ItemDTO("name", "name", false)
    }

}