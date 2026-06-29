package com.orbix.ui.repository

import com.orbix.ui.local.TokenStorage
import com.orbix.ui.model.CreateRentalRequest
import com.orbix.ui.model.RentalResponse
import com.orbix.ui.service.ApiClient
import com.orbix.ui.service.ApiResult
import com.orbix.ui.util.parseApiError
import retrofit2.HttpException

class RentalRepository(
    private val tokenStorage: TokenStorage? = null
) {
    private suspend fun ensureAuthHeader() {
        tokenStorage?.syncToApiClient()
    }

    suspend fun create(
        vehicleId: Long,
        fechaInicio: String,
        fechaFin: String
    ): ApiResult<RentalResponse> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(
                ApiClient.rentalApi.create(
                    CreateRentalRequest(vehicleId, fechaInicio, fechaFin)
                )
            )
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error(parseApiError(e, "Sesión expirada. Inicia sesión de nuevo."))
                403 -> ApiResult.Error(parseApiError(e, "No tienes permiso para solicitar esta renta."))
                400 -> ApiResult.Error(parseApiError(e, "Datos inválidos para la solicitud."))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    suspend fun myRequests(): ApiResult<List<RentalResponse>> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(ApiClient.rentalApi.myRequests())
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error(parseApiError(e, "Sesión expirada. Inicia sesión de nuevo."))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar solicitudes")
        }
    }

    suspend fun receivedRequests(): ApiResult<List<RentalResponse>> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(ApiClient.rentalApi.receivedRequests())
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error(parseApiError(e, "Sesión expirada. Inicia sesión de nuevo."))
                403 -> ApiResult.Error(parseApiError(e, "Solo arrendadores pueden ver solicitudes recibidas."))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar solicitudes")
        }
    }

    suspend fun approve(id: Long): ApiResult<RentalResponse> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(ApiClient.rentalApi.approve(id))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error(parseApiError(e, "Sesión expirada. Inicia sesión de nuevo."))
                400, 403 -> ApiResult.Error(parseApiError(e))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al aprobar solicitud")
        }
    }

    suspend fun reject(id: Long): ApiResult<RentalResponse> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(ApiClient.rentalApi.reject(id))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error(parseApiError(e, "Sesión expirada. Inicia sesión de nuevo."))
                400, 403 -> ApiResult.Error(parseApiError(e))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al rechazar solicitud")
        }
    }
}
