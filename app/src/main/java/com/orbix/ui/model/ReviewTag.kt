package com.orbix.ui.model

enum class ReviewTag {
    ANFITRION_AMABLE,
    RESPUESTA_RAPIDA,
    MUY_PUNTUAL,
    INSTRUCCIONES_CLARAS,
    RECOMENDADO,
    RESPETUOSO,
    CUIDO_EL_VEHICULO
}

fun ReviewTag.label(): String = when (this) {
    ReviewTag.ANFITRION_AMABLE -> "Anfitrión amable"
    ReviewTag.RESPUESTA_RAPIDA -> "Respuesta rápida"
    ReviewTag.MUY_PUNTUAL -> "Muy puntual"
    ReviewTag.INSTRUCCIONES_CLARAS -> "Instrucciones claras"
    ReviewTag.RECOMENDADO -> "Recomendado"
    ReviewTag.RESPETUOSO -> "Respetuoso"
    ReviewTag.CUIDO_EL_VEHICULO -> "Cuidó el vehículo"
}

val vehicleReviewTags = listOf(
    ReviewTag.ANFITRION_AMABLE,
    ReviewTag.RESPUESTA_RAPIDA,
    ReviewTag.MUY_PUNTUAL,
    ReviewTag.INSTRUCCIONES_CLARAS,
    ReviewTag.RECOMENDADO
)

val userReviewTags = listOf(
    ReviewTag.MUY_PUNTUAL,
    ReviewTag.RESPETUOSO,
    ReviewTag.CUIDO_EL_VEHICULO
)
