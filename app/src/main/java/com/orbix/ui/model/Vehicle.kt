package com.orbix.ui.model

data class Vehicle(
    val id: Long,
    val brand: String,
    val model: String,
    val pricePerDay: Double,
    val imageUrl: String,
    val available: Boolean
)
