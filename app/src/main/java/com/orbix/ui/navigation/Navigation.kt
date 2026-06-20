package com.orbix.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Home

@Serializable
data class CarDetail(val carId: String)

@Serializable
object NewVehicle

@Serializable
data class TermsAndConditions(val isSignUpFlow: Boolean = false)

@Serializable
object SignUp

@Serializable
object CarReview

@Serializable
object UserReview

@Serializable
object Favorites

@Serializable
object IDVerification

@Serializable
object Search
