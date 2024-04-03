package com.hika.data.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hika.data.model.CartEntity

@Database(
    entities = [CartEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScentDatabase: RoomDatabase() {
    abstract fun scentDao(): ScentDao
}