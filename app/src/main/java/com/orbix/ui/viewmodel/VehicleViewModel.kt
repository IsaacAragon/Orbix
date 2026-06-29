package com.orbix.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.Vehicle
import com.orbix.ui.repository.VehicleRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch

class VehicleViewModel : ViewModel() {

    private val repository = VehicleRepository()

    var vehicles by mutableStateOf(listOf<Vehicle>())
        private set

    var vehicleDetail by mutableStateOf<Vehicle?>(null)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadVehicles(roles: Collection<String>) {
        viewModelScope.launch {
            isRefreshing = true
            errorMessage = null
            when (val result = repository.loadVehicles(roles)) {
                is ApiResult.Success -> vehicles = result.data
                is ApiResult.Error -> errorMessage = result.message
            }
            isRefreshing = false
        }
    }

    fun loadVehicleDetail(id: Long) {
        viewModelScope.launch {
            when (val result = repository.getVehicleById(id)) {
                is ApiResult.Success -> vehicleDetail = result.data
                is ApiResult.Error -> errorMessage = result.message
            }
        }
    }

    fun vehicleById(id: Long): Vehicle? =
        vehicleDetail?.takeIf { it.id == id }
            ?: vehicles.find { it.id == id }
}
