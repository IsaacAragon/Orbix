package com.orbix.ui.model

data class Vehicle(
    val id: Long? = null,
    val brand: String,
    val model: String,
    val year: String?,
    val transmission: String?,
    val passengers: String?,
    val pricePerDay: Double?,
    val imageUrl: String?,
    val available: Boolean?,
    val description: String?,
    val category: VehicleCategory?
) {
    val isAvailable: Boolean get() = available != false
}
