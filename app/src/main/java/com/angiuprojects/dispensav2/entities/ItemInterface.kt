package com.angiuprojects.dispensav2.entities

import java.lang.reflect.Method

open class ItemInterface() {
    lateinit var name: String

    constructor(name: String) : this() {
        this.name = name
    }
    open fun getFieldList() : List<String> {
        return listOf("name")
    }

    open fun getFieldFromMethod(m: Method) : String? {
        return when(m.name) {
            name -> getFieldList()[0]
            else -> null
        }
    }

}