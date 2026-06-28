package com.orbix.ui.repository

import com.orbix.ui.local.TokenStorage
import com.orbix.ui.model.AuthResponse
import com.orbix.ui.model.LoginRequest
import com.orbix.ui.model.RegisterRequest
import com.orbix.ui.service.ApiClient
import retrofit2.HttpException

sealed class AuthResult {
    data class Success(val response: AuthResponse) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

data class UserSession(
    val email: String,
    val roles: Set<String>,
    val permissions: Set<String>
)

class AuthRepository(
    private val tokenStorage: TokenStorage
) {
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = ApiClient.authApi.login(LoginRequest(email, password))
            saveSession(response)
            AuthResult.Success(response)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> AuthResult.Error("Credenciales inválidas")
                403 -> AuthResult.Error("No tienes permiso para acceder")
                else -> AuthResult.Error("Error del servidor")
            }
        } catch (e: Exception) {
            AuthResult.Error("Error de conexión. Verifica que el backend esté activo.")
        }
    }

    suspend fun register(email: String, password: String, nombre: String): AuthResult {
        return try {
            val response = ApiClient.authApi.register(
                RegisterRequest(email = email, password = password, nombre = nombre)
            )
            saveSession(response)
            AuthResult.Success(response)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.Error("Datos inválidos o el correo ya está registrado")
                else -> AuthResult.Error("Error del servidor")
            }
        } catch (e: Exception) {
            AuthResult.Error("Error de conexión. Verifica que el backend esté activo.")
        }
    }

    suspend fun saveSession(response: AuthResponse) {
        tokenStorage.saveSession(response)
    }

    suspend fun restoreSession(): UserSession? {
        val token = tokenStorage.getToken() ?: return null
        ApiClient.setToken(token)

        return try {
            val response = ApiClient.authApi.me()
            saveSession(response)
            UserSession(
                email = response.email,
                roles = response.roles.toSet(),
                permissions = response.permissions.toSet()
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                clearSession()
            }
            null
        } catch (e: Exception) {
            val email = tokenStorage.getEmail()
            if (email != null) {
                UserSession(
                    email = email,
                    roles = tokenStorage.getRoles(),
                    permissions = tokenStorage.getPermissions()
                )
            } else {
                null
            }
        }
    }

    suspend fun clearSession() {
        tokenStorage.clearSession()
    }
}
