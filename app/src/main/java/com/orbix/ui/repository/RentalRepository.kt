package com.orbix.ui.repository

import com.orbix.ui.model.CreateRentalRequest
import com.orbix.ui.model.RentalResponse
import com.orbix.ui.model.CreateExtensionRequest
import com.orbix.ui.model.ExtensionResponse
import com.orbix.ui.service.ApiClient
import com.orbix.ui.service.ApiResult
import com.orbix.ui.util.parseApiError
import retrofit2.HttpException

class RentalRepository {
    private suspend fun ensureAuthHeader() {
        if (ApiClient.ensureTokenLoaded().isNullOrBlank()) {
            throw IllegalStateException("Sesión expirada. Inicia sesión de nuevo.")
        }
    }

    private fun <T> authError(e: IllegalStateException): ApiResult<T> =
        ApiResult.Error(e.message ?: "Sesión expirada. Inicia sesión de nuevo.")

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
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error(parseApiError(e, "No tienes permiso para solicitar esta renta."))
                400 -> ApiResult.Error(parseApiError(e, "Datos inválidos para la solicitud."))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            ApiResult.Error(e.message ?: "Sesión expirada. Inicia sesión de nuevo.")
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
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            authError(e)
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
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error(parseApiError(e, "Solo arrendadores pueden ver solicitudes recibidas."))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            authError(e)
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
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                400, 403 -> ApiResult.Error(parseApiError(e))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            authError(e)
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
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                400, 403 -> ApiResult.Error(parseApiError(e))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            authError(e)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al rechazar solicitud")
        }
    }

    suspend fun createExtension(rentalId: Long, diasExtension: Int): ApiResult<ExtensionResponse> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(
                ApiClient.rentalApi.createExtension(
                    rentalId, CreateExtensionRequest(diasExtension)
                )
            )
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error(parseApiError(e, "No tienes permiso para solicitar esta extensión."))
                400 -> ApiResult.Error(parseApiError(e, "Datos inválidos para la extensión."))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            ApiResult.Error(e.message ?: "Sesión expirada. Inicia sesión de nuevo.")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    suspend fun getExtensionsForRental(rentalId: Long): ApiResult<List<ExtensionResponse>> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(ApiClient.rentalApi.getExtensionsForRental(rentalId))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            authError(e)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar las extensiones")
        }
    }

    suspend fun approveExtension(extensionId: Long): ApiResult<ExtensionResponse> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(ApiClient.rentalApi.approveExtension(extensionId))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                400, 403 -> ApiResult.Error(parseApiError(e))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            authError(e)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al aprobar la extensión")
        }
    }

    suspend fun rejectExtension(extensionId: Long): ApiResult<ExtensionResponse> {
        return try {
            ensureAuthHeader()
            ApiResult.Success(ApiClient.rentalApi.rejectExtension(extensionId))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                400, 403 -> ApiResult.Error(parseApiError(e))
                else -> ApiResult.Error(parseApiError(e))
            }
        } catch (e: IllegalStateException) {
            authError(e)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al rechazar la extensión")
        }
    }
}
