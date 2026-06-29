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
    private const val BASE_URL = "https://api.agrolab.tech/api/"

    private var appContext: Context? = null
    @Volatile
    private var token: String? = null
    private var tokenStorage: TokenStorage? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        if (tokenStorage == null) {
            tokenStorage = TokenStorage(context.applicationContext)
        }
    }

    fun setToken(value: String?) {
        if (!value.isNullOrBlank()) {
            token = value
        }
    }

    fun clearToken() {
        token = null
    }

    fun getToken(): String? = token?.takeIf { it.isNotBlank() }

    private fun storage(): TokenStorage? {
        tokenStorage?.let { return it }
        return appContext?.let { ctx ->
            TokenStorage(ctx).also { tokenStorage = it }
        }
    }

    /** Carga el token desde memoria o DataStore antes de peticiones autenticadas. */
    suspend fun ensureTokenLoaded(): String? {
        getToken()?.let { return it }
        val stored = storage()?.getToken()?.takeIf { it.isNotBlank() }
        if (stored != null) {
            token = stored
        }
        return stored
    }

    private fun resolveToken(): String? {
        getToken()?.let { return it }
        val stored = storage()?.let { storage ->
            runBlocking { storage.getToken()?.takeIf { it.isNotBlank() } }
        }
        if (stored != null) {
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
    val rentalApi: RentalApi by lazy { retrofit.create(RentalApi::class.java) }
}
