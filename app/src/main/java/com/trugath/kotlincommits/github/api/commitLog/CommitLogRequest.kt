package com.trugath.kotlincommits.github.api.commitLog

import com.trugath.kotlincommits.github.api.ErrorResponse
import com.trugath.kotlincommits.github.api.Request
import com.trugath.kotlincommits.github.api.Response
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json.JSONException

class CommitLogRequest(
    private val owner: String,
    private val repo: String,
    private val until: DateTime?,
    private val after: DateTime?) : Request {
    private val timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZoneUTC()

    override fun endpoint(): String {
        return "/repos/$owner/$repo/commits"
    }

    override fun parameters(): Map<String, String> {
        return mapOf(
            "until" to until?.toString(timeFormatter),
            "since" to after?.toString(timeFormatter)
        ).filterValues { it != null }.mapValues { it.value ?: "" }
    }

    override fun process(response: String): Response {
        return try {
            CommitLogResponseImpl(response)
        } catch(e: JSONException) {
            ErrorResponse()
        }
    }
}