package com.trugath.kotlincommits

import android.app.Application
import androidx.room.Room
import com.trugath.kotlincommits.db.CommitDao
import com.trugath.kotlincommits.db.CommitDatabase

class KotlinCommits : Application() {

    companion object {
        private var commitDb: CommitDatabase? = null
        fun commits(): CommitDao = commitDb!!.commitDao()
    }

    override fun onCreate() {
        super.onCreate()
        commitDb = Room.databaseBuilder(
            applicationContext,
            CommitDatabase::class.java, "commits"
        ).enableMultiInstanceInvalidation()
            .build()
    }
}