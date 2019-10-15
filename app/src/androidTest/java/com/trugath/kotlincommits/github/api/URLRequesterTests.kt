package com.trugath.kotlincommits.github.api

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class URLRequesterTests{

    /**
     * Checks basic functionality of the URLRequester by requesting the top level schema and
     * verifying some assumptions
     */
    @Test
    fun schemaCheck() {
        runBlocking {
            val requester = URLRequester("https://api.github.com")
            when(val response = requester.request(SimpleRequest(""))) {
                is SimpleResponse -> {
                    assertEquals("https://api.github.com/repos/{owner}/{repo}",
                        response.string("repository_url"))
                }
                else -> fail()
            }
            when(val response = requester.request(SimpleRequest("/repos/JetBrains/kotlin"))) {
                is SimpleResponse -> {
                    assertEquals("https://api.github.com/repos/JetBrains/kotlin/commits{/sha}",
                        response.string("commits_url"))
                }
                else -> fail()
            }
        }
    }
}

