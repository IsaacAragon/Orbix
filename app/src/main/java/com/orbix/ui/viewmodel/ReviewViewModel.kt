package com.orbix.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbix.ui.model.AllReviewTagsResponse
import com.orbix.ui.model.ReviewTagOption
import com.orbix.ui.model.UserReviewResponse
import com.orbix.ui.model.UserReviewSummary
import com.orbix.ui.model.VehicleReviewResponse
import com.orbix.ui.model.VehicleReviewSummary
import com.orbix.ui.repository.ReviewRepository
import com.orbix.ui.service.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    companion object {
        const val TYPE_VEHICLE = "VEHICLE"
        const val TYPE_USER = "USER"
    }

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
    var isLoadingTags by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _availableTags = MutableStateFlow<List<ReviewTagOption>>(emptyList())
    val availableTags = _availableTags.asStateFlow()

    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    val selectedTags = _selectedTags.asStateFlow()

    private var allTags: AllReviewTagsResponse? = null
    private var reviewType = TYPE_VEHICLE
    private var lastRating = 5

    fun setReviewType(type: String) {
        reviewType = type
        if (allTags != null) {
            updateTagsForRating(lastRating)
        }
    }

    fun loadAllTags() {
        viewModelScope.launch {
            isLoadingTags = true
            when (val result = repository.getAllReviewTags()) {
                is ApiResult.Success -> {
                    allTags = result.data
                    updateTagsForRating(lastRating)
                }
                is ApiResult.Error -> errorMessage = result.message
            }
            isLoadingTags = false
        }
    }

    fun onRatingChanged(rating: Int) {
        lastRating = rating
        updateTagsForRating(rating)
    }

    private fun updateTagsForRating(rating: Int) {
        _selectedTags.value = emptySet()
        val tags = when (reviewType) {
            TYPE_USER -> allTags?.user?.get(rating)
            else -> allTags?.vehicle?.get(rating)
        } ?: emptyList()
        _availableTags.value = tags
    }

    fun toggleTag(code: String) {
        _selectedTags.value = _selectedTags.value.toMutableSet().apply {
            if (contains(code)) remove(code) else add(code)
        }
    }

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
        comment: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null

            when (
                val result = repository.createVehicleReview(
                    vehicleId,
                    rating,
                    _selectedTags.value.toList(),
                    comment
                )
            ) {
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
        comment: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null

            when (
                val result = repository.createUserReview(
                    reviewedUserId,
                    rating,
                    _selectedTags.value.toList(),
                    comment
                )
            ) {
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
