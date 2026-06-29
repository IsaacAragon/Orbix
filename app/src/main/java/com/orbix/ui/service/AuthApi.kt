package com.orbix.ui.service

import com.orbix.ui.model.AuthResponse
import com.orbix.ui.model.ClientProfileResponse
import com.orbix.ui.model.LoginRequest
import com.orbix.ui.model.RegisterRequest
import com.orbix.ui.model.UpdatePhoneRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @GET("auth/me")
    suspend fun me(): AuthResponse

    @GET("auth/profile")
    suspend fun getProfile(): ClientProfileResponse

    @PATCH("auth/phone")
    suspend fun updatePhone(@Body body: UpdatePhoneRequest): AuthResponse
}
