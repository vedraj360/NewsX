package com.vdx.newsx.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vdx.newsx.domain.models.Multimedia
import java.util.*

class ListTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromMultiMediaList(multiMediaList: List<Multimedia>): String {
            return Gson().toJson(multiMediaList)
        }

        @TypeConverter
        @JvmStatic
        fun toMultiMediaList(multiMedia: String): List<Multimedia> {
            val listType = object : TypeToken<ArrayList<Multimedia>>() {}.type
            return Gson().fromJson(multiMedia, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringList(stringList: List<String>): String {
            return Gson().toJson(stringList)
        }

        @TypeConverter
        @JvmStatic
        fun toStringList(stringList: String): List<String> {
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(stringList, listType)
        }
    }
}
