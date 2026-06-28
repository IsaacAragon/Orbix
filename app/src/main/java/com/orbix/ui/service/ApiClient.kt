package com.orbix.ui.service

import android.content.Context
import com.orbix.ui.local.TokenStorage
import com.orbix.ui.model.AuthInterceptor
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8082/api/"

    private var token: String? = null
    private var tokenStorage: TokenStorage? = null

    fun init(context: Context) {
        if (tokenStorage == null) {
            tokenStorage = TokenStorage(context.applicationContext)
        }
    }

    fun setToken(value: String?) {
        token = value
    }

    fun getToken(): String? = token

    private fun resolveToken(): String? {
        token?.takeIf { it.isNotBlank() }?.let { return it }
        val stored = tokenStorage?.let { storage ->
            runBlocking { storage.getToken() }
        }
        if (!stored.isNullOrBlank()) {
            token = stored
        }
        return stored
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor { resolveToken() })
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val vehicleApi: VehicleService by lazy { retrofit.create(VehicleService::class.java) }
    val reviewApi: ReviewApi by lazy { retrofit.create(ReviewApi::class.java) }
}
