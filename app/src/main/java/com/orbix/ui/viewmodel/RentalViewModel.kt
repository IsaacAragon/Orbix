package com.orbix.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.RentalResponse
import com.orbix.ui.model.ExtensionResponse
import com.orbix.ui.repository.RentalRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch

class RentalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RentalRepository()

    var myRentals by mutableStateOf<List<RentalResponse>>(emptyList())
        private set
    var receivedRentals by mutableStateOf<List<RentalResponse>>(emptyList())
        private set
    var extensionRequests by mutableStateOf<List<ExtensionResponse>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isSubmitting by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var submitErrorMessage by mutableStateOf<String?>(null)
        private set
    var successMessage by mutableStateOf<String?>(null)
        private set

    fun clearMessages() {
        errorMessage = null
        submitErrorMessage = null
        successMessage = null
    }

    fun rentalForVehicle(vehicleId: Long): RentalResponse? =
        myRentals.filter { it.vehicleId == vehicleId }
            .maxByOrNull { it.fechaSolicitud }

    fun loadMyRentals() {
        viewModelScope.launch {
            isLoading = true
            when (val result = repository.myRequests()) {
                is ApiResult.Success -> myRentals = result.data
                is ApiResult.Error -> { /* no bloquear la pantalla si falla la precarga */ }
            }
            isLoading = false
        }
    }

    fun loadReceivedRentals() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            when (val result = repository.receivedRequests()) {
                is ApiResult.Success -> receivedRentals = result.data
                is ApiResult.Error -> errorMessage = result.message
            }
            isLoading = false
        }
    }

    fun loadExtensionsForRental(rentalId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            when (val result = repository.getExtensionsForRental(rentalId)) {
                is ApiResult.Success -> extensionRequests = result.data
                is ApiResult.Error -> errorMessage = result.message
            }
            isLoading = false
        }
    }

    fun createRental(
        vehicleId: Long,
        fechaInicio: String,
        fechaFin: String,
        onSuccess: (RentalResponse) -> Unit = {}
    ) {
        viewModelScope.launch {
            isSubmitting = true
            submitErrorMessage = null
            successMessage = null
            when (val result = repository.create(vehicleId, fechaInicio, fechaFin)) {
                is ApiResult.Success -> {
                    myRentals = listOf(result.data) + myRentals.filter { it.id != result.data.id }
                    successMessage = "¡Solicitud enviada! El propietario revisará tu solicitud pronto."
                    onSuccess(result.data)
                }
                is ApiResult.Error -> submitErrorMessage = result.message
            }
            isSubmitting = false
        }
    }

    fun createExtension(rentalId: Long, diasExtension: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isSubmitting = true
            submitErrorMessage = null
            successMessage = null
            when (val result = repository.createExtension(rentalId, diasExtension)) {
                is ApiResult.Success -> {
                    extensionRequests = listOf(result.data) + extensionRequests.filter { it.id != result.data.id }
                    // Also update the local rental's fechaFin in myRentals (if we want to reflect it immediately? No, it's just a pending extension request, it hasn't changed the actual rental fechaFin yet)
                    successMessage = "¡Solicitud de extensión enviada!"
                    onSuccess()
                }
                is ApiResult.Error -> submitErrorMessage = result.message
            }
            isSubmitting = false
        }
    }

    fun approveRequest(id: Long) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null
            when (val result = repository.approve(id)) {
                is ApiResult.Success -> {
                    receivedRentals = receivedRentals.map {
                        if (it.id == id) result.data else it
                    }
                }
                is ApiResult.Error -> errorMessage = result.message
            }
            isSubmitting = false
        }
    }

    fun rejectRequest(id: Long) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null
            when (val result = repository.reject(id)) {
                is ApiResult.Success -> {
                    receivedRentals = receivedRentals.map {
                        if (it.id == id) result.data else it
                    }
                }
                is ApiResult.Error -> errorMessage = result.message
            }
            isSubmitting = false
        }
    }

    fun approveExtension(extensionId: Long, onSuccess: (ExtensionResponse) -> Unit = {}) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null
            when (val result = repository.approveExtension(extensionId)) {
                is ApiResult.Success -> {
                    extensionRequests = extensionRequests.map {
                        if (it.id == extensionId) result.data else it
                    }
                    // Update myRentals/receivedRentals to reflect new end date
                    myRentals = myRentals.map {
                        if (it.id == result.data.rentaId) {
                            it.copy(fechaFin = result.data.nuevaFechaFin)
                        } else it
                    }
                    receivedRentals = receivedRentals.map {
                        if (it.id == result.data.rentaId) {
                            it.copy(fechaFin = result.data.nuevaFechaFin)
                        } else it
                    }
                    successMessage = "¡Extensión aprobada correctamente!"
                    onSuccess(result.data)
                }
                is ApiResult.Error -> errorMessage = result.message
            }
            isSubmitting = false
        }
    }

    fun rejectExtension(extensionId: Long, onSuccess: (ExtensionResponse) -> Unit = {}) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null
            when (val result = repository.rejectExtension(extensionId)) {
                is ApiResult.Success -> {
                    extensionRequests = extensionRequests.map {
                        if (it.id == extensionId) result.data else it
                    }
                    successMessage = "Solicitud de extensión rechazada."
                    onSuccess(result.data)
                }
                is ApiResult.Error -> errorMessage = result.message
            }
            isSubmitting = false
        }
    }
}

