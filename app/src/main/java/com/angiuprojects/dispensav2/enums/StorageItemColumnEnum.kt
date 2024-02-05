package com.angiuprojects.dispensav2.enums

enum class StorageItemColumnEnum(val fieldName: String, val columnName: String, val isNullable: Boolean) {
    NAME("Name", "name", false),
    SECTION("Section", "section", false),
    PROFILE("Profile", "profile", false),
    QUANTITY("Quantity", "quantity", false),
    TRIGGER("Trigger", "trigger_value", false),
    EXP_DATE("ExpirationDate", "exp_date", true);
}