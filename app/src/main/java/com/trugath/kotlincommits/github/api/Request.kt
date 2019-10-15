package com.trugath.kotlincommits.github.api

interface Request {
    fun endpoint(): String
    fun parameters(): Map<String, String> {
        return emptyMap()
    }
    fun process(response: String): Response
}