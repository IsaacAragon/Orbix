package com.orbix.ui.service

import com.orbix.ui.model.CreateVehicleRequest
import com.orbix.ui.model.Vehicle
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VehicleService {
    @GET("vehicles")
    suspend fun getAllVehicles(): List<Vehicle>

    @GET("vehicles/mine")
    suspend fun getMyVehicles(): List<Vehicle>

    @GET("vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: Long): Vehicle

    @POST("vehicles")
    suspend fun create(@Body vehicle: CreateVehicleRequest): Vehicle
}
