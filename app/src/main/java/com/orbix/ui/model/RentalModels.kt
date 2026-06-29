package com.orbix.ui.model

enum class RentalStatus {
    PENDIENTE, APROBADA, RECHAZADA
}

fun RentalStatus.label(): String = when (this) {
    RentalStatus.PENDIENTE -> "Pendiente"
    RentalStatus.APROBADA -> "Aprobada"
    RentalStatus.RECHAZADA -> "Rechazada"
}

data class CreateRentalRequest(
    val vehicleId: Long,
    val fechaInicio: String,
    val fechaFin: String
)

data class RentalResponse(
    val id: Long,
    val vehicleId: Long,
    val vehicleBrand: String,
    val vehicleModel: String,
    val vehicleImageUrl: String?,
    val clienteId: Long,
    val clienteNombre: String?,
    val clienteEmail: String,
    val ownerId: Long?,
    val ownerNombre: String?,
    val fechaInicio: String,
    val fechaFin: String,
    val estado: RentalStatus,
    val fechaSolicitud: String,
    val totalDias: Long,
    val totalPrecio: Double,
    val canReviewCliente: Boolean = false,
    val clienteAlreadyReviewed: Boolean = false
)
