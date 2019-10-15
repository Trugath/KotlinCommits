package com.trugath.kotlincommits.github.api.commitLog

import com.trugath.kotlincommits.db.CommitEntry
import com.trugath.kotlincommits.github.api.JsonArrayResponse
import org.json.JSONObject
import org.joda.time.format.DateTimeFormat

internal class CommitLogResponseImpl(response: String) : JsonArrayResponse(response),
    CommitLogResponse {
    private val timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZoneUTC()
    override fun entries(): List<CommitEntry> {
        return (0 until length())
            .map(this::get)
            .filterIsInstance<JSONObject>()
            .map { entry ->
                val sha = entry.getString("sha")
                val commit = entry.getJSONObject("commit")
                val commitDate = timeFormatter.parseDateTime( commit.getJSONObject("committer").getString("date") )
                val authorName = commit.getJSONObject("author").getString("name")
                val author: JSONObject? = if(JSONObject.NULL == entry.get("author")){
                    null
                } else {
                    entry.optJSONObject("author")
                }
                val authorAvatar: String? = author?.getString("avatar_url")
                val title = commit.getString("message")
                CommitEntry(
                    sha = sha,
                    timeStamp = commitDate,
                    authorName = authorName,
                    authorAvatar = authorAvatar,
                    title = title
                )
            }
    }
}