package com.orbix.ui.model

data class ClientProfileResponse(
    val userId: Long,
    val nombre: String,
    val email: String,
    val memberSinceYear: Int?,
    val reviewSummary: UserReviewSummary,
    val reviews: List<UserReviewResponse>
)
