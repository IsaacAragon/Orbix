package com.orbix.ui.service

import com.orbix.ui.model.Vehicle
import retrofit2.http.GET

interface VehicleService {

    @GET("api/vehicles")
    suspend fun getVehicles(): List<Vehicle>
}