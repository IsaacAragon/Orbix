package com.orbix.ui.repository

import com.orbix.ui.model.CreateVehicleRequest
import com.orbix.ui.model.Vehicle
import com.orbix.ui.model.VehicleCategory
import com.orbix.ui.service.ApiClient
import com.orbix.ui.service.ApiResult
import retrofit2.HttpException

class VehicleRepository {
    suspend fun getVehicles(): ApiResult<List<Vehicle>> {
        return try {
            ApiResult.Success(ApiClient.vehicleApi.getVehicles())
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error("No tienes permiso para ver vehículos.")
                else -> ApiResult.Error("Error del servidor")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    suspend fun createVehicle(
        brand: String,
        model: String,
        year: String,
        transmission: String,
        passengers: String,
        pricePerDay: Double,
        description: String,
        category: VehicleCategory
    ): ApiResult<Vehicle> {
        return try {
            ApiResult.Success(
                ApiClient.vehicleApi.create(
                    CreateVehicleRequest(
                        brand = brand,
                        model = model,
                        year = year,
                        transmission = transmission,
                        passengers = passengers,
                        pricePerDay = pricePerDay,
                        imageUrl = null,
                        available = true,
                        description = description,
                        category = category
                    )
                )
            )
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error("No tienes permiso para publicar vehículos.")
                else -> ApiResult.Error("Error del servidor")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }
}
