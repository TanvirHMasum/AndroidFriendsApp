package com.example.friends.services

import com.example.friends.data.ConstantData
import com.google.gson.GsonBuilder
import com.squareup.picasso.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {
    fun get(): ApiService = api

    private val api: ApiService
        get() = client.create(ApiService::class.java)

    private val client: Retrofit
        get() {
            val gson = GsonBuilder().serializeNulls().create()
            return Retrofit.Builder()
                .baseUrl(ConstantData.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttp)
                .build()
        }

    private val okHttp: okhttp3.OkHttpClient
        get() {
            val okHttpBuilder = okhttp3.OkHttpClient.Builder()
            okHttpBuilder.readTimeout(60, TimeUnit.SECONDS)
            okHttpBuilder.writeTimeout(60, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpBuilder.addNetworkInterceptor(loggingInterceptor)
            }

            return okHttpBuilder.build()
        }
}