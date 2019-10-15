package com.trugath.kotlincommits.github.api.commitLog

import com.trugath.kotlincommits.db.CommitEntry

interface CommitLogResponse {
    fun entries(): List<CommitEntry>
}