package com.orbix.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.OrbixApplication
import com.orbix.ui.model.AuthResponse
import com.orbix.ui.repository.AuthResult
import com.orbix.ui.util.validateRegister
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository =
        (application as OrbixApplication).authRepository

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(
        nombre: String,
        email: String,
        password: String,
        confirmPassword: String,
        fechaNacimiento: String?,
        onSuccess: (AuthResponse) -> Unit
    ) {
        validateRegister(email, password, confirmPassword, fechaNacimiento)?.let {
            errorMessage = it
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (
                val result = authRepository.register(
                    email = email,
                    password = password,
                    nombre = nombre,
                    fechaNacimiento = fechaNacimiento
                )
            ) {
                is AuthResult.Success -> onSuccess(result.response)
                is AuthResult.Error -> errorMessage = result.message
            }

            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
