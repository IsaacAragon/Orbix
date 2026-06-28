package com.orbix.ui.service

import com.orbix.ui.model.CreateVehicleRequest
import com.orbix.ui.model.Vehicle
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VehicleService {
    @GET("vehicles")
    suspend fun getVehicles(): List<Vehicle>

    @POST("vehicles")
    suspend fun create(@Body vehicle: CreateVehicleRequest): Vehicle
}
