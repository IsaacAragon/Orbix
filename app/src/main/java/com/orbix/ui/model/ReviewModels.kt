package com.orbix.ui.model

data class CreateVehicleReviewBody(
    val rating: Int,
    val tags: List<String>,
    val comment: String?
)

data class VehicleReviewResponse(
    val id: Long,
    val rating: Int,
    val tags: List<String>,
    val comment: String?,
    val reviewerId: Long,
    val reviewerName: String,
    val vehicleId: Long,
    val vehicleBrand: String,
    val vehicleModel: String,
    val fecha: String
)

data class VehicleReviewSummary(
    val vehicleId: Long,
    val brand: String,
    val model: String,
    val ownerName: String?,
    val averageRating: Double,
    val totalReviews: Long,
    val sentimentLabel: String
)

data class CreateUserReviewRequest(
    val reviewedUserId: Long,
    val rating: Int,
    val tags: List<String>,
    val comment: String?
)

data class UserReviewResponse(
    val id: Long,
    val rating: Int,
    val tags: List<String>,
    val comment: String?,
    val reviewerId: Long,
    val reviewerName: String,
    val reviewedUserId: Long,
    val reviewedUserName: String,
    val fecha: String
)

data class UserReviewSummary(
    val userId: Long,
    val nombre: String,
    val averageRating: Double,
    val totalReviews: Long,
    val sentimentLabel: String,
    val memberSinceYear: Int?
)
