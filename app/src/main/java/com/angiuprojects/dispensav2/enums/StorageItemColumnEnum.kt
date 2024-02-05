package com.angiuprojects.dispensav2.enums

enum class StorageItemColumnEnum(val fieldName: String, val columnName: String) {
    NAME("Name", "name"),
    SECTION("Section", "section"),
    PROFILE("Profile", "profile"),
    QUANTITY("Quantity", "quantity"),
    TRIGGER("Trigger", "trigger_value"),
    EXP_DATE("ExpirationDate", "exp_date");

    fun getColumnFromField(){
        //todo
    }
}