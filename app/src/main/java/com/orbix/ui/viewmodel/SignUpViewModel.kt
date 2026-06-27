package com.orbix.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.local.TokenStorage
import com.orbix.ui.repository.AuthRepository
import com.orbix.ui.repository.AuthResult
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(TokenStorage(application))

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(nombre: String, email: String, password: String, onSuccess: () -> Unit) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val result = authRepository.register(email, password, nombre)) {
                is AuthResult.Success -> onSuccess()
                is AuthResult.Error -> errorMessage = result.message
            }

            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
