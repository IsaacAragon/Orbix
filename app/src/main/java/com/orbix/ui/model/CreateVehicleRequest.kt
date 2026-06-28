package com.orbix.ui.model

data class CreateVehicleRequest(
    val brand: String,
    val model: String,
    val year: String,
    val transmission: String?,
    val passengers: String?,
    val pricePerDay: Double,
    val imageUrl: String?,
    val available: Boolean = true,
    val description: String,
    val category: VehicleCategory
)
