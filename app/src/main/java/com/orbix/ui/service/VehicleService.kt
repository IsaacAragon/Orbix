package com.orbix.ui.service

import com.orbix.ui.model.Vehicle
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VehicleService {
    @GET("vehicles")
    suspend fun getVehicles(): List<Vehicle>

    @POST("vehicles")
    suspend fun create(@Body vehicle: VehicleRequest): Vehicle
}

data class VehicleRequest(
    val brand: String,
    val model: String,
    val year: String,
    val transmission: String,
    val passengers: String,
    val pricePerDay: Double,
    val imageUrl: String = ""
)
