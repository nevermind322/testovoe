package com.example.testovoe.api

import com.squareup.moshi.Json


sealed class ServerLoginResponse{
    data class Success(val response: LoginResponse) : ServerLoginResponse()
    data class Error(
        val error: ErrorResponse
    ) : ServerLoginResponse()
}

sealed class ServerPaymentsResponse {
    data class Success(val response: List<PaymentResponseJson>) : ServerPaymentsResponse()
    data class Error(
        val error: ErrorResponse
    ) : ServerPaymentsResponse()
}

data class ErrorResponse(
    @Json(name = "error_code") val code: Int,
    @Json(name = "error_msg") val error: String
)

data class LoginResponse(val token: String)

data class PaymentResponseJson(val id: Int, val title: String, val amount: String?, val created: String?)
