package com.ismet.parkingzonemaddevs.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ismet.parkingzonemaddevs.data.model.Corner

class Converters {
    @TypeConverter
    fun toArrCorner(value: String): ArrayList<Corner> {
        val listType = object : TypeToken<ArrayList<Corner>>() {}.type
        return Gson().fromJson<ArrayList<Corner>>(value, listType)
    }

    @TypeConverter
    fun fromArrCorner(list: ArrayList<Corner>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toArrString(value: String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson<ArrayList<String>>(value, listType)
    }

    @TypeConverter
    fun fromArrString(list: ArrayList<String>): String {
        return Gson().toJson(list)
    }


}