package com.trugath.kotlincommits.github.api

interface Requester {
    /**
     * Perform the request and return the response
     */
    suspend fun request(request: Request): Response
}