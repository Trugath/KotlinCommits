package com.trugath.kotlincommits.github.api

class SimpleResponseImpl(response: String) : JsonObjectResponse(response), SimpleResponse {
    override fun string(entry: String): String {
        return getString(entry)
    }
}