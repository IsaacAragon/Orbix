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
    val description: String? = null,
    val category: String? = null,
    val ownerId: Long? = null,
    val ownerName: String? = null,
    val ownerPhone: String? = null,
    val isFavorite: Boolean = false
) {
    val isAvailable: Boolean get() = available != false
}
