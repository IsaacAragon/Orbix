package com.orbix.ui.repository

import com.orbix.ui.model.CreateVehicleRequest
import com.orbix.ui.model.Vehicle
import com.orbix.ui.model.Transmission
import com.orbix.ui.model.VehicleCategory
import com.orbix.ui.service.ApiClient
import com.orbix.ui.service.ApiResult
import com.orbix.ui.util.Roles
import retrofit2.HttpException

class VehicleRepository {

    private suspend fun ensureAuthHeader() {
        if (ApiClient.ensureTokenLoaded().isNullOrBlank()) {
            throw IllegalStateException("Sesión expirada. Inicia sesión de nuevo.")
        }
    }

    suspend fun loadVehicles(roles: Collection<String>): ApiResult<List<Vehicle>> {
        return try {
            val vehicles = if (Roles.isArrendador(roles.toSet())) {
                ensureAuthHeader()
                ApiClient.vehicleApi.getMyVehicles()
            } else {
                ApiClient.vehicleApi.getAllVehicles()
            }
            ApiResult.Success(vehicles)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error("No tienes permiso para ver vehículos.")
                else -> ApiResult.Error("Error del servidor")
            }
        } catch (e: IllegalStateException) {
            ApiResult.Error(e.message ?: "Sesión expirada. Inicia sesión de nuevo.")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    suspend fun getVehicleById(id: Long): ApiResult<Vehicle> {
        return try {
            ApiResult.Success(ApiClient.vehicleApi.getVehicleById(id))
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> ApiResult.Error("Vehículo no encontrado")
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
        transmission: Transmission,
        passengers: String,
        pricePerDay: Double,
        description: String,
        category: VehicleCategory
    ): ApiResult<Vehicle> {
        return try {
            ensureAuthHeader()
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
        } catch (e: IllegalStateException) {
            ApiResult.Error(e.message ?: "Sesión expirada. Inicia sesión de nuevo.")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }
}
