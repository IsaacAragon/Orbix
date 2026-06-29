package com.orbix.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.ClientProfileResponse
import com.orbix.ui.repository.ProfileRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProfileRepository()

    var profile by mutableStateOf<ClientProfileResponse?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
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
}
