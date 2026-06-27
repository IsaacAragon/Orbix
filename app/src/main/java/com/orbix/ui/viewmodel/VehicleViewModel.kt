package com.orbix.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.Vehicle
import com.orbix.ui.repository.VehicleRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class VehicleViewModel : ViewModel() {
    private val repository =
        VehicleRepository()

    var vehicles by mutableStateOf(
        listOf<Vehicle>()
    )

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        viewModelScope.launch {
            when (val result = repository.getVehicles()) {
                is ApiResult.Success -> vehicles = result.data
                is ApiResult.Error -> {}
            }
        }
    }
}