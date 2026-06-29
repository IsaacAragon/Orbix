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

    var favoriteVehicles by mutableStateOf(
        listOf<Vehicle>()
    )

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        loadVehicles()
        loadFavorites()
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

    fun loadFavorites() {
        viewModelScope.launch {
            when (val result = repository.getFavorites()) {
                is ApiResult.Success -> favoriteVehicles = result.data
                is ApiResult.Error -> {}
            }
        }
    }

    fun toggleFavorite(vehicle: Vehicle) {
        viewModelScope.launch {
            val id = vehicle.id ?: return@launch
            val wasFavorite = vehicle.isFavorite

            // Optimistic update for vehicles list
            vehicles = vehicles.map {
                if (it.id == id) it.copy(isFavorite = !wasFavorite) else it
            }

            // Optimistic update for favoriteVehicles list
            if (wasFavorite) {
                favoriteVehicles = favoriteVehicles.filter { it.id != id }
            } else {
                if (favoriteVehicles.none { it.id == id }) {
                    favoriteVehicles = favoriteVehicles + vehicle.copy(isFavorite = true)
                }
            }

            val result = if (wasFavorite) {
                repository.removeFavorite(id)
            } else {
                repository.addFavorite(id)
            }

            if (result is ApiResult.Error) {
                // Revert on error
                vehicles = vehicles.map {
                    if (it.id == id) it.copy(isFavorite = wasFavorite) else it
                }
                if (wasFavorite) {
                    if (favoriteVehicles.none { it.id == id }) {
                        favoriteVehicles = favoriteVehicles + vehicle.copy(isFavorite = true)
                    }
                } else {
                    favoriteVehicles = favoriteVehicles.filter { it.id != id }
                }
            }
        }
    }
}