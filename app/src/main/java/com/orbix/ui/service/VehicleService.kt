package com.orbix.ui.service

import com.orbix.ui.model.CreateVehicleRequest
import com.orbix.ui.model.Vehicle
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path

interface VehicleService {
    @GET("vehicles")
    suspend fun getVehicles(): List<Vehicle>

    @POST("vehicles")
    suspend fun create(@Body vehicle: CreateVehicleRequest): Vehicle

    @GET("favorites")
    suspend fun getFavorites(): List<Vehicle>

    @POST("favorites/{vehicleId}")
    suspend fun addFavorite(@Path("vehicleId") id: Long)

    @DELETE("favorites/{vehicleId}")
    suspend fun removeFavorite(@Path("vehicleId") id: Long)
}
