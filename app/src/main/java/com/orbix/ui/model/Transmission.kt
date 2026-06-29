package com.orbix.ui.model

enum class Transmission {
    AUTOMATICO,
    MANUAL
}

fun Transmission.label(): String = when (this) {
    Transmission.AUTOMATICO -> "Automático"
    Transmission.MANUAL -> "Manual"
}
