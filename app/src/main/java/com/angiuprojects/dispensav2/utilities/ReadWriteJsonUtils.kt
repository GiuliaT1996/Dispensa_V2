package com.angiuprojects.dispensav2.utilities

import android.content.Context
import android.util.Log
import com.angiuprojects.dispensav2.entities.ProfileSettings
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class ReadWriteJsonUtils {

    companion object {
        lateinit var singleton: ReadWriteJsonUtils

        fun initializeReadWriteJsonUtils() {
            singleton = ReadWriteJsonUtils()
        }
    }

    private val fileName = "StorageAppProfileSettings.txt"
    private val directory = "Storage App Profile Settings"

    fun getProfileSettings(context: Context) {
        val dir = getDirectory(context)
        val json = read(dir, context)
        Log.i(Constants.STORAGE_LOGGER, "READ -> Json: $json")
        if(json != "") {
            try {
                Constants.profileSettings = Gson().fromJson(json, ProfileSettings::class.java)
                Log.i(Constants.STORAGE_LOGGER, "ProfileSettings " + Constants.profileSettings.toString())
                return
            } catch (e: Exception) {
                Log.e(Constants.STORAGE_LOGGER, "Error converting file in json")
            }
        }
        Constants.profileSettings = ProfileSettings()
    }

    fun write(context: Context) {

        val dir = getDirectory(context)
        try {
            val file = File(dir, fileName)
            val writer = FileWriter(file)
            val json = Gson().toJson(Constants.profileSettings)
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

    private fun read(dir: File, context: Context) : String {
        try {
            val file = File(dir, fileName)
            if (!file.exists()) {
                Log.e(Constants.STORAGE_LOGGER, "File does not exist")
                write(context)
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