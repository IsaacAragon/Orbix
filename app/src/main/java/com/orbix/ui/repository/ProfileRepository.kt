package com.orbix.ui.repository

import com.orbix.ui.model.ClientProfileResponse
import com.orbix.ui.service.ApiClient
import com.orbix.ui.service.ApiResult
import com.orbix.ui.util.parseApiError
import retrofit2.HttpException

class ProfileRepository {
    suspend fun getProfile(): ApiResult<ClientProfileResponse> {
        return try {
            if (ApiClient.ensureTokenLoaded().isNullOrBlank()) {
                return ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
            }
            ApiResult.Success(ApiClient.authApi.getProfile())
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error(parseApiError(e, "Sesión expirada. Inicia sesión de nuevo."))
                else -> ApiResult.Error(parseApiError(e, "No se pudo cargar el perfil."))
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }
}
