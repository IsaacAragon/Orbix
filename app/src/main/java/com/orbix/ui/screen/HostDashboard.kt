package com.orbix.ui.screen

data class HostDashboard(
    //TODO ESTO SERA REEMPLAZADO POR API, ESTO SOLAMENTE ES UN DEMO
    val activeCars: Int,
    val availableCars: Int,
    val activeRentals: Int,
    val totalEarnings: Double,
    val rating: Double,
    val totalReviews: Int
)
