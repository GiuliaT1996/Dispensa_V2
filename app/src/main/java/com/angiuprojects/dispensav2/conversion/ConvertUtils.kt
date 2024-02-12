package com.angiuprojects.dispensav2.conversion

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConvertUtils {

    companion object {

        lateinit var singleton : ConvertUtils

        fun initializeConvertUtils() {
            singleton = ConvertUtils()
        }
    }

    lateinit var dispensaItemList: MutableList<DispensaItem>

    fun getItems() = runBlocking {

        launch {
            OldQueries.singleton.getItems()
        }
    }

}