package com.orbix.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.Vehicle
import com.orbix.ui.repository.VehicleRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class VehicleViewModel : ViewModel() {
    private val repository =
        VehicleRepository()

    var vehicles by mutableStateOf(
        listOf<Vehicle>()
    )

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        viewModelScope.launch {
            isRefreshing = true
            delay(2000) // Keep progress indicator visible for 2 seconds to give visual feedback
            when (val result = repository.getVehicles()) {
                is ApiResult.Success -> vehicles = result.data
                is ApiResult.Error -> {}
            }
            isRefreshing = false
        }
    }
}