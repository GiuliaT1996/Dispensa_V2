package com.angiuprojects.dispensav2.entities

import java.time.LocalDate
import java.util.SortedMap

class MealPlan (val weekMap: SortedMap<LocalDate, String>){

    override fun toString(): String {
        val s : StringBuilder = StringBuilder()
        s.append("MealPlan: {\n")
        weekMap.forEach { s.append("Giorno: ").append(it.key.dayOfMonth).append("/").append(it.key.monthValue)
            .append(" Pasto previsto: ").append(it.value)}
        s.append("}")
        return s.toString()
    }
}