package com.orbix.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.OrbixApplication
import com.orbix.ui.model.ClientProfileResponse
import com.orbix.ui.repository.AuthResult
import com.orbix.ui.repository.ProfileRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProfileRepository()
    private val authRepository = (application as OrbixApplication).authRepository

    var profile by mutableStateOf<ClientProfileResponse?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isUpdatingPhone by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var phoneErrorMessage by mutableStateOf<String?>(null)
        private set
    var savedTelefono by mutableStateOf<String?>(null)
        private set

    fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            when (val result = repository.getProfile()) {
                is ApiResult.Success -> profile = result.data
                is ApiResult.Error -> errorMessage = result.message
            }
            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun updatePhone(telefono: String, onSuccess: (String?) -> Unit) {
        if (telefono.isBlank()) {
            phoneErrorMessage = "El teléfono es obligatorio para arrendadores"
            return
        }
        viewModelScope.launch {
            isUpdatingPhone = true
            phoneErrorMessage = null
            when (val result = authRepository.updatePhone(telefono)) {
                is AuthResult.Success -> {
                    savedTelefono = result.response.telefono
                    onSuccess(result.response.telefono)
                }
                is AuthResult.Error -> phoneErrorMessage = result.message
            }
            isUpdatingPhone = false
        }
    }

    fun clearPhoneError() {
        phoneErrorMessage = null
    }
}
