package com.example.testovoe.api

import com.example.testovoe.login.Credentials
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

const val appKey= "12345"
const val version = "1"

interface EasypayService {

    @Headers(
        "app-key: $appKey",
        "v: $version"
    )
    @GET("payments")
    suspend fun getPayments(@Header("token") token : String)

    @Headers(
        "app-key: $appKey",
        "v: $version"
    )
    @POST("login")
    suspend fun authorize(@Body creds : Credentials) : ServerAnswer
}