package com.orbix.ui.service

import com.orbix.ui.model.CreateRentalRequest
import com.orbix.ui.model.RentalResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RentalApi {
    @POST("rentals")
    suspend fun create(@Body body: CreateRentalRequest): RentalResponse

    @GET("rentals/mine")
    suspend fun myRequests(): List<RentalResponse>

    @GET("rentals/received")
    suspend fun receivedRequests(): List<RentalResponse>

    @PATCH("rentals/{id}/approve")
    suspend fun approve(@Path("id") id: Long): RentalResponse

    @PATCH("rentals/{id}/reject")
    suspend fun reject(@Path("id") id: Long): RentalResponse
}
