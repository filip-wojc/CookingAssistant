package com.cookingassistant.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

class RetrofitClient{
    private val BASE_URL = "http://192.168.52.183:5080/api/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    // configure OkHttpClient for logging, authentication
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(ApiRepository::class.java)
    }
}