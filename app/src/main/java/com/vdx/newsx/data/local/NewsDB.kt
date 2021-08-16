package com.vdx.newsx.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vdx.newsx.domain.models.Result

@Database(entities = [Result::class], version = 1, exportSchema = false)
@TypeConverters(ListTypeConverter::class)
abstract class NewsDB : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}