package com.example.testovoe.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://easypay.world/api-test/"

val moshi: Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .add(
        PolymorphicJsonAdapterFactory.of(
            ServerLoginResponse::class.java,
            "success"
        ).withSubtype(
            ServerLoginResponse.Success::class.java, "true"
        ).withSubtype(ServerLoginResponse.Error::class.java, "false")
    )
    .add(
        PolymorphicJsonAdapterFactory.of(
            ServerPaymentsResponse::class.java,
            "success"
        ).withSubtype(
            ServerPaymentsResponse.Success::class.java, "true"
        ).withSubtype(ServerPaymentsResponse.Error::class.java, "false")
    )
    .build()

val apiClient = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
    .create(EasypayService::class.java)