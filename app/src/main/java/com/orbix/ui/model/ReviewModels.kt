package com.orbix.ui.model

data class CreateVehicleReviewBody(
    val rating: Int,
    val tags: List<String>,
    val comment: String?
)

data class VehicleReviewResponse(