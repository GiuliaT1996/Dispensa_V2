package com.angiuprojects.dispensav2.conversion

import android.os.Handler
import android.os.Looper
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
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