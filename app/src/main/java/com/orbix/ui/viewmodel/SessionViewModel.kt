package com.orbix.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.OrbixApplication
import com.orbix.ui.model.AuthResponse
import com.orbix.ui.repository.AuthRepository
import com.orbix.ui.repository.UserSession
import com.orbix.ui.service.ApiClient
import kotlinx.coroutines.launch

sealed class SessionState {
    data object Loading : SessionState()
    data object Unauthenticated : SessionState()
    data class Authenticated(val session: UserSession) : SessionState()
}

class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository =
        (application as OrbixApplication).authRepository

    var sessionState by mutableStateOf<SessionState>(SessionState.Loading)
        private set

    init {
        restoreSession()
    }

    fun restoreSession() {
        viewModelScope.launch {
            sessionState = SessionState.Loading
            val session = authRepository.restoreSession()
            sessionState = if (session != null) {
                SessionState.Authenticated(session)
            } else {
                SessionState.Unauthenticated
            }
        }
    }

    fun onLoginSuccess(session: UserSession) {
        sessionState = SessionState.Authenticated(session)
    }

    fun onLoginSuccessFromResponse(response: AuthResponse) {
        response.token?.takeIf { it.isNotBlank() }?.let { ApiClient.setToken(it) }
        viewModelScope.launch {
            authRepository.saveSession(response)
        }
        sessionState = SessionState.Authenticated(
            UserSession(
                email = response.email,
                roles = response.roles.toSet(),
                permissions = response.permissions.toSet(),
                userId = response.userId,
                nombre = response.nombre
            )
        )
    }

    fun logout(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            authRepository.clearSession()
            sessionState = SessionState.Unauthenticated
            onComplete()
        }
    }

    val permissions: Set<String>
        get() = (sessionState as? SessionState.Authenticated)?.session?.permissions ?: emptySet()

    val email: String
        get() = (sessionState as? SessionState.Authenticated)?.session?.email ?: ""

    val roles: Set<String>
        get() = (sessionState as? SessionState.Authenticated)?.session?.roles ?: emptySet()
}
