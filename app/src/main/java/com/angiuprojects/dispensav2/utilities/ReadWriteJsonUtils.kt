package com.angiuprojects.dispensav2.utilities

import android.content.Context
import android.util.Log
import com.angiuprojects.dispensav2.entities.MealPlan
import com.angiuprojects.dispensav2.entities.ProfileSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.time.LocalDate


class ReadWriteJsonUtils {

    companion object {
        lateinit var singleton: ReadWriteJsonUtils

        fun initializeReadWriteJsonUtils() {
            singleton = ReadWriteJsonUtils()
        }
    }

    val profileFileName = "StorageAppProfileSettings.txt"
    val mealPlanFileName = "MealPlanSettings.txt"
    private val directory = "Storage App Settings"

    fun getProfileSettings(context: Context) {
        val dir = getDirectory(context)
        val json = read(dir, context, profileFileName, Constants.profileSettings)
        Log.i(Constants.STORAGE_LOGGER, "READ -> Json: $json")
        if(json != "") {
            try {
                Constants.profileSettings = Gson().fromJson(json, ProfileSettings::class.java)
                Log.i(Constants.STORAGE_LOGGER, "ProfileSettings " + Constants.profileSettings.toString())
                return
            } catch (e: Exception) {
                Log.e(Constants.STORAGE_LOGGER, "Error converting file in json. \n" + e.message)
            }
        }
        Constants.profileSettings = ProfileSettings()
    }

    fun getMealPlan(context: Context) {
        val dir = getDirectory(context)
        val json = read(dir, context, mealPlanFileName, Constants.mealPlan)
        Log.i(Constants.STORAGE_LOGGER, "READ -> Json: $json")
        if(json != "") {
            try {
                val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java,
                    JsonDeserializer<Any?> { j, _, _ -> LocalDate.parse(j.asString) })
                    .create()
                Constants.mealPlan = gson.fromJson(json, MealPlan::class.java)
                Log.i(Constants.STORAGE_LOGGER, "MealPlan " + Constants.mealPlan.toString())
                return
            } catch (e: Exception) {
                Log.e(Constants.STORAGE_LOGGER, "Error converting file in json. \n" + e.message)
            }
        }
        Constants.mealPlan = MealPlan(sortedMapOf())
    }

    fun <T> write(context: Context, fileName: String, objToWrite: T) {

        val dir = getDirectory(context)
        try {
            val file = File(dir, fileName)
            val writer = FileWriter(file)
            val json = Gson().toJson(objToWrite)
            Log.i(Constants.STORAGE_LOGGER, "WRITE -> Json: $json")
            writer.append(json)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(Constants.STORAGE_LOGGER, "Error reading or saving file")
        }
    }

    private fun getDirectory(context: Context) : File {
        val dir = File(context.filesDir, directory)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    private fun <T> read(dir: File, context: Context, fileName: String, objToWrite: T) : String {
        try {
            val file = File(dir, fileName)
            if (!file.exists()) {
                Log.e(Constants.STORAGE_LOGGER, "File does not exist")
                write(context, fileName, objToWrite)
                return ""
            }
            val reader = FileReader(file)
            return reader.readText()
        } catch (e: Exception) {
            Log.e(Constants.STORAGE_LOGGER, "Error converting file in json")
        }
        return ""
    }
}