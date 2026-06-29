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
        set

    // Form fields persisted across screen navigation
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var telefono by mutableStateOf("")
    var birthDateDisplay by mutableStateOf("")
    var birthDateApi by mutableStateOf<String?>(null)
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    fun register(
        onSuccess: (AuthResponse) -> Unit
    ) {
        validateRegister(email, password, confirmPassword, birthDateApi, telefono)?.let {
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
                    nombre = fullName,
                    telefono = telefono.takeIf { it.isNotBlank() },
                    fechaNacimiento = birthDateApi
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

    fun clearForm() {
        fullName = ""
        email = ""
        telefono = ""
        birthDateDisplay = ""
        birthDateApi = null
        password = ""
        confirmPassword = ""
        errorMessage = null
    }
}
