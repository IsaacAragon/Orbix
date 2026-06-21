package com.orbix.ui.screen
//TODO ESTO SERA REEMPLAZADO POR API, ESTO SOLAMENTE ES UN DEMO
data class ManagedRental(
val id: String,
val carName: String,
val userName: String,
val startDate: String,
val endDate: String,
val status: RentalStatus
)

enum class RentalStatus {
    PENDING,
    ACTIVE,
    COMPLETED,
    CANCELLED
}
