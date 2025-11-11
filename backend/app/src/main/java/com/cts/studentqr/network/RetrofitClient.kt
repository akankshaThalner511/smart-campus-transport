package com.cts.studentqr.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ✅ Singleton object for Retrofit
object RetrofitClient {

    // <-- Make sure this IP is accessible from your phone
    private const val BASE_URL = "http://192.168.1.27:8092/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
