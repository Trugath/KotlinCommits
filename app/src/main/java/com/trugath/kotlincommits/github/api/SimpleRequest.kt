package com.trugath.kotlincommits.github.api

class SimpleRequest(private val endpoint: String) : Request {
    override fun endpoint(): String {
        return endpoint
    }

    override fun process(response: String): Response {
        return SimpleResponseImpl(response)
    }
}