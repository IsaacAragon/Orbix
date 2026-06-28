package com.orbix.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.VehicleCategory
import com.orbix.ui.repository.VehicleRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch

class NewVehicleViewModel : ViewModel() {

    private val repository = VehicleRepository()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun createVehicle(
        brand: String,
        model: String,
        year: String,
        transmission: String,
        passengers: String,
        pricePerDay: String,
        description: String,
        category: VehicleCategory?,
        onSuccess: () -> Unit
    ) {
        val price = pricePerDay.toDoubleOrNull()
        if (brand.isBlank() || model.isBlank() || year.isBlank() ||
            transmission.isBlank() || passengers.isBlank() || price == null
        ) {
            errorMessage = "Completa todos los campos correctamente"
            return
        }
        if (description.isBlank()) {
            errorMessage = "La descripción es obligatoria"
            return
        }
        if (category == null) {
            errorMessage = "Selecciona una categoría"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (
                val result = repository.createVehicle(
                    brand = brand,
                    model = model,
                    year = year,
                    transmission = transmission,
                    passengers = passengers,
                    pricePerDay = price,
                    description = description,
                    category = category
                )
            ) {
                is ApiResult.Success -> onSuccess()
                is ApiResult.Error -> errorMessage = result.message
            }

            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
