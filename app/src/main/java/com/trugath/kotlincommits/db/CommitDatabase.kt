package com.trugath.kotlincommits.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CommitEntry::class], version = 1)
@TypeConverters(Converters::class)
abstract class CommitDatabase : RoomDatabase() {
    abstract fun commitDao(): CommitDao
}