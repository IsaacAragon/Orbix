package com.orbix.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.ReviewTag
import com.orbix.ui.model.UserReviewResponse
import com.orbix.ui.model.UserReviewSummary
import com.orbix.ui.model.VehicleReviewResponse
import com.orbix.ui.model.VehicleReviewSummary
import com.orbix.ui.repository.ReviewRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    private val repository = ReviewRepository()

    var vehicleSummary by mutableStateOf<VehicleReviewSummary?>(null)
        private set
    var vehicleReviews by mutableStateOf<List<VehicleReviewResponse>>(emptyList())
        private set
    var userSummary by mutableStateOf<UserReviewSummary?>(null)
        private set
    var userReviews by mutableStateOf<List<UserReviewResponse>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set
    var isSubmitting by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadVehicleReviews(vehicleId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val summaryResult = repository.getVehicleSummary(vehicleId)) {
                is ApiResult.Success -> vehicleSummary = summaryResult.data
                is ApiResult.Error -> errorMessage = summaryResult.message
            }
            when (val reviewsResult = repository.getVehicleReviews(vehicleId)) {
                is ApiResult.Success -> vehicleReviews = reviewsResult.data
                is ApiResult.Error -> if (errorMessage == null) errorMessage = reviewsResult.message
            }

            isLoading = false
        }
    }

    fun loadUserReviews(userId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val summaryResult = repository.getUserSummary(userId)) {
                is ApiResult.Success -> userSummary = summaryResult.data
                is ApiResult.Error -> errorMessage = summaryResult.message
            }
            when (val reviewsResult = repository.getUserReviews(userId)) {
                is ApiResult.Success -> userReviews = reviewsResult.data
                is ApiResult.Error -> if (errorMessage == null) errorMessage = reviewsResult.message
            }

            isLoading = false
        }
    }

    fun submitVehicleReview(
        vehicleId: Long,
        rating: Int,
        tags: List<ReviewTag>,
        comment: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null

            when (val result = repository.createVehicleReview(vehicleId, rating, tags, comment)) {
                is ApiResult.Success -> {
                    loadVehicleReviews(vehicleId)
                    onSuccess()
                }
                is ApiResult.Error -> errorMessage = result.message
            }

            isSubmitting = false
        }
    }

    fun submitUserReview(
        reviewedUserId: Long,
        rating: Int,
        tags: List<ReviewTag>,
        comment: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null

            when (val result = repository.createUserReview(reviewedUserId, rating, tags, comment)) {
                is ApiResult.Success -> {
                    loadUserReviews(reviewedUserId)
                    onSuccess()
                }
                is ApiResult.Error -> errorMessage = result.message
            }

            isSubmitting = false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
