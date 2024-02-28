package com.hika.myscent.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hika.myscent.model.CartEntity

@Database(
    entities = [CartEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScentDatabase: RoomDatabase() {
    abstract fun scentDao(): ScentDao
}