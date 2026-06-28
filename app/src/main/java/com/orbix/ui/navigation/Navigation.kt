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
data class CarReview(val vehicleId: Long)

@Serializable
data class UserReview(val reviewedUserId: Long)

@Serializable
object Favorites

@Serializable
object IDVerification

@Serializable
object Search

@Serializable
object RentalDetail

@Serializable
object HostDetail

@Serializable
object HostDashboard

@Serializable
object CarManagement

@Serializable
object RentalManagement

@Serializable
object HostRules