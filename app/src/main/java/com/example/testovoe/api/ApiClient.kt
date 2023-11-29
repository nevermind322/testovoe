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
            ServerAnswer::class.java,
            "success"
        ).withSubtype(
            ServerAnswer.SuccessAnswer::class.java, "true"
        ).withSubtype(ServerAnswer.ErrorAnswer::class.java, "false")
    )
    .build()

val apiClient = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
    .create(EasypayService::class.java)