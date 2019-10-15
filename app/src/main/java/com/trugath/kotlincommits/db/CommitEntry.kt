package com.trugath.kotlincommits.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity
data class CommitEntry(
    @PrimaryKey(autoGenerate = false) val sha: String,
    @ColumnInfo(name = "timeStamp") val timeStamp: DateTime,
    @ColumnInfo(name = "authorName") val authorName: String,
    @ColumnInfo(name = "authorAvatar") val authorAvatar: String?,
    @ColumnInfo(name = "title") val title: String
)
