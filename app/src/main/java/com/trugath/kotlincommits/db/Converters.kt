package com.trugath.kotlincommits.db

import androidx.room.TypeConverter
import org.joda.time.DateTime
import org.joda.time.Instant

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): DateTime? {
        return value?.let { Instant.ofEpochMilli(it).toDateTime() }
    }

    @TypeConverter
    @JvmStatic
    fun dateTimeToTimestamp(dateTime: DateTime): Long {
        return dateTime.millis
    }
}