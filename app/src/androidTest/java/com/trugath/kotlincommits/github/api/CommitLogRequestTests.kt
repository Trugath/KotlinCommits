package com.trugath.kotlincommits.github.api

import com.trugath.kotlincommits.db.CommitEntry
import com.trugath.kotlincommits.github.api.commitLog.CommitLogRequest
import com.trugath.kotlincommits.github.api.commitLog.CommitLogResponse
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class CommitLogRequestTests {

    @Test
    fun testEndpointString() {
        val request = CommitLogRequest(
            "JetBrains",
            "kotlin",
            null,
            null
        )
        assertEquals("/repos/JetBrains/kotlin/commits", request.endpoint())
    }

    @Test
    fun testParameters() {
        val timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZoneUTC()

        assertEquals(
            emptyMap<String, String>(),
            CommitLogRequest(
                "JetBrains",
                "kotlin",
                null,
                null
            ).parameters()
        )

        val beforeString = "2019-01-01T02:03:04Z"
        val before = DateTime.parse(beforeString, timeFormatter)
        assertEquals(
            mapOf("until" to beforeString),
            CommitLogRequest(
                "JetBrains",
                "kotlin",
                before,
                null
            ).parameters()
        )

        val afterString = "2019-01-01T02:03:04Z"
        val after = DateTime.parse(afterString, timeFormatter)
        assertEquals(
            mapOf("since" to beforeString),
            CommitLogRequest(
                "JetBrains",
                "kotlin",
                null,
                after
            ).parameters()
        )

        assertEquals(
            mapOf("until" to beforeString, "since" to beforeString),
            CommitLogRequest(
                "JetBrains",
                "kotlin",
                before,
                after
            ).parameters()
        )
    }

    @Test
    fun mockedEmptyResponse() {
        val mockRequester = object : Requester {
            override suspend fun request(request: Request): Response {
                val response = """[]"""
                return request.process(response)
            }
        }

        val response = runBlocking {
            (mockRequester.request(
                CommitLogRequest(
                    "JetBrains",
                    "kotlin",
                    null,
                    null
                )
            ) as CommitLogResponse).entries()
        }
        assertEquals(emptyList<CommitEntry>(), response)
    }

    @Test
    fun mockedResponseNullAuthor() {
        val mockRequester = object : Requester {
            override suspend fun request(request: Request): Response {
                val response = """[{
	"sha": "1111111111111111111111111111111111111111",
	"commit": {
		"author": {
			"name": "author name"
		},
		"committer": {
			"date": "2019-02-03T04:05:06Z"
		},
		"message": "Commit Message"
	},
	"author": null,
	"committer": null
}]"""
                return request.process(response)
            }
        }

        val response = runBlocking {
            (mockRequester.request(
                CommitLogRequest(
                    "JetBrains",
                    "kotlin",
                    null,
                    null
                )
            ) as CommitLogResponse).entries()
        }

        val timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZoneUTC()
        assertEquals(listOf(CommitEntry(
            sha = "1111111111111111111111111111111111111111",
            title = "Commit Message",
            authorAvatar = null,
            authorName = "author name",
            timeStamp = timeFormatter.parseDateTime("2019-02-03T04:05:06Z")
        )), response)
    }


    @Test
    fun mockedResponseWithAvatar() {
        val mockRequester = object : Requester {
            override suspend fun request(request: Request): Response {
                val response = """[{
	"sha": "1111111111111111111111111111111111111111",
	"commit": {
		"author": {
			"name": "author name"
		},
		"committer": {
			"date": "2019-02-03T04:05:06Z"
		},
		"message": "Commit Message"
	},
	"author": {
      "avatar_url": "https://example.com/authorAvatar.gif"
    },
	"committer": null
}]"""
                return request.process(response)
            }
        }

        val response = runBlocking {
            (mockRequester.request(
                CommitLogRequest(
                    "JetBrains",
                    "kotlin",
                    null,
                    null
                )
            ) as CommitLogResponse).entries()
        }

        val timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZoneUTC()
        assertEquals(listOf(CommitEntry(
            sha = "1111111111111111111111111111111111111111",
            title = "Commit Message",
            authorAvatar = "https://example.com/authorAvatar.gif",
            authorName = "author name",
            timeStamp = timeFormatter.parseDateTime("2019-02-03T04:05:06Z")
        )), response)
    }
}