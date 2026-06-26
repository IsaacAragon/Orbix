package com.orbix.ui.service

import com.orbix.ui.model.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var token: String? = null
    fun setToken(value: String?) { token = value }
    val authApi: AuthApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor { token })
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8082/api/")  // emulador
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}