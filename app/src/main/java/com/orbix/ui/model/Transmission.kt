package com.orbix.ui.model

enum class Transmission {
    AUTOMATICO,
    MANUAL
}

fun Transmission.label(): String = when (this) {
    Transmission.AUTOMATICO -> "Automático"
    Transmission.MANUAL -> "Manual"
}

fun transmissionLabel(value: String?): String {
    if (value.isNullOrBlank()) return "—"
    return runCatching { Transmission.valueOf(value).label() }.getOrElse { value }
}
