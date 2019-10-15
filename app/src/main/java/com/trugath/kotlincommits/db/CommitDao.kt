package com.trugath.kotlincommits.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CommitDao {
    @Query("SELECT * FROM CommitEntry ORDER BY timeStamp DESC")
    fun getAll(): LiveData<List<CommitEntry>>

    @Query("SELECT * FROM CommitEntry ORDER BY timeStamp ASC LIMIT 1")
    fun getOldest(): CommitEntry?

    @Query("SELECT * FROM CommitEntry ORDER BY timeStamp DESC LIMIT 1")
    fun getNewest(): CommitEntry?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg entries: CommitEntry)

    @Delete
    suspend fun delete(entry: CommitEntry)
}
