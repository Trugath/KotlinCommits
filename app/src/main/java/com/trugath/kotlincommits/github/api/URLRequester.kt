package com.trugath.kotlincommits.github.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class URLRequester(private val baseUrl: String) : Requester {
    override suspend fun request(request: Request): Response {
        try {
            val response = withContext(Dispatchers.IO) {
                val paramString = if (request.parameters().isNotEmpty()) {
                    request
                        .parameters()
                        .map {
                            "${it.key}=${it.value}"
                        }.joinToString(prefix = "?", separator = "&")
                } else ""
                val requestString = baseUrl + request.endpoint() + paramString
                val url = URL(requestString)
                val inputStream = with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    addRequestProperty("Accept", "application/vnd.github.v3+json")
                    BufferedReader(InputStreamReader(inputStream))
                }
                val sb = StringBuilder()
                for (line in inputStream.readLine()) {
                    sb.append(line)
                }
                sb.toString()
            }
            return request.process(response)
        } catch (e: Exception) {
            return ErrorResponse()
        }
    }
}
