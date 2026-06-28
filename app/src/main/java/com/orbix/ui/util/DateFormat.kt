package com.orbix.ui.util

fun formatFecha(fecha: String): String {
    return try {
        val datePart = fecha.substringBefore('T')
        val parts = datePart.split('-')
        if (parts.size == 3) {
            val monthNames = listOf(
                "ene", "feb", "mar", "abr", "may", "jun",
                "jul", "ago", "sep", "oct", "nov", "dic"
            )
            val monthIndex = parts[1].toIntOrNull()?.minus(1)
            val month = monthIndex?.let { monthNames.getOrNull(it) } ?: parts[1]
            "${parts[2].toInt()} $month ${parts[0]}"
        } else {
            fecha
        }
    } catch (_: Exception) {
        fecha
    }
}
