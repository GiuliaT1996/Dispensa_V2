package com.angiuprojects.dispensav2.entities

import com.angiuprojects.dispensav2.enums.ComparatorEnum
import com.angiuprojects.dispensav2.enums.ConditionEnum

class WhereCondition (val field: String, val value: String?, val comparator: ComparatorEnum?, val condition: ConditionEnum, val isNull: Boolean?){

    fun buildCondition(isLastCondition: Boolean) : String {
        return field + (if(isNull != null && !isNull) " IS NOT NULL AND $field " else if (isNull != null && isNull)" IS NULL OR $field " else " ") + "${comparator?.symbol} $value" + if (isLastCondition) " " else " ${condition.name} "
    }
}