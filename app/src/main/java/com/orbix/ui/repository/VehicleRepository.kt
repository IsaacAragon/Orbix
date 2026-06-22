package com.orbix.ui.repository

import com.orbix.ui.model.Vehicle
import com.orbix.ui.service.ApiResult
import com.orbix.ui.service.RetrofitClient

class VehicleRepository {
    suspend fun getVehicles():
            ApiResult<List<Vehicle>> {

        return try {

            ApiResult.Success(
                RetrofitClient.api.getVehicles()
            )

        } catch (e: Exception){

            ApiResult.Error(
                e.message ?: "Error"
            )
        }
    }
}