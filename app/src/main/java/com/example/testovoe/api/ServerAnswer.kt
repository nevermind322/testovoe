package com.example.testovoe.api

import com.squareup.moshi.Json


sealed class ServerAnswer {
    data class SuccessAnswer(val response: Response) : ServerAnswer()
    data class ErrorAnswer(
        val error: Error
    ) : ServerAnswer()
}

data class Error(
    @Json(name = "error_code") val code: Int,
    @Json(name = "error_msg") val error: String
)

data class Response(val token: String)