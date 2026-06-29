package com.orbix.ui.service

import com.orbix.ui.model.CreateRentalRequest
import com.orbix.ui.model.RentalResponse
import com.orbix.ui.model.CreateExtensionRequest
import com.orbix.ui.model.ExtensionResponse
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

    @POST("rentals/{rentalId}/extensions")
    suspend fun createExtension(
        @Path("rentalId") rentalId: Long,
        @Body body: CreateExtensionRequest
    ): ExtensionResponse

    @PATCH("rentals/extensions/{extensionId}/approve")
    suspend fun approveExtension(@Path("extensionId") extensionId: Long): ExtensionResponse

    @PATCH("rentals/extensions/{extensionId}/reject")
    suspend fun rejectExtension(@Path("extensionId") extensionId: Long): ExtensionResponse

    @GET("rentals/{rentalId}/extensions")
    suspend fun getExtensionsForRental(@Path("rentalId") rentalId: Long): List<ExtensionResponse>

    @GET("rentals/extensions/mine")
    suspend fun getMyExtensions(): List<ExtensionResponse>

    @GET("rentals/extensions/received")
    suspend fun getReceivedExtensions(): List<ExtensionResponse>
}

