package com.orbix.ui.service

import com.orbix.ui.model.CreateUserReviewRequest
import com.orbix.ui.model.CreateVehicleReviewRequest
import com.orbix.ui.model.UserReviewResponse
import com.orbix.ui.model.UserReviewSummary
import com.orbix.ui.model.VehicleReviewResponse
import com.orbix.ui.model.VehicleReviewSummary
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {
    @POST("reviews")
    suspend fun createVehicleReview(@Body body: CreateVehicleReviewRequest): VehicleReviewResponse

    @GET("reviews/vehicle/{vehicleId}")
    suspend fun getVehicleReviews(@Path("vehicleId") vehicleId: Long): List<VehicleReviewResponse>

    @GET("reviews/vehicle/{vehicleId}/summary")
    suspend fun getVehicleSummary(@Path("vehicleId") vehicleId: Long): VehicleReviewSummary

    @POST("reviews/user")
    suspend fun createUserReview(@Body body: CreateUserReviewRequest): UserReviewResponse

    @GET("reviews/user/{userId}")
    suspend fun getUserReviews(@Path("userId") userId: Long): List<UserReviewResponse>

    @GET("reviews/user/{userId}/summary")
    suspend fun getUserSummary(@Path("userId") userId: Long): UserReviewSummary
}
