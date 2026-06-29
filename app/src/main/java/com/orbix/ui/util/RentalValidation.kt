package com.orbix.ui.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun validateRentalDates(start: LocalDate, end: LocalDate): String? {
    if (end.isBefore(start)) return "La fecha fin debe ser posterior o igual a la de inicio"
    if (start.isBefore(LocalDate.now())) return "La fecha inicio no puede ser pasada"
    return null
}

fun rentalDaysBetween(inicio: String, fin: String): Long {
    val start = LocalDate.parse(inicio)
    val end = LocalDate.parse(fin)
    return ChronoUnit.DAYS.between(start, end) + 1
}

fun formatDateRange(inicio: String, fin: String): String {
    val start = LocalDate.parse(inicio)
    val end = LocalDate.parse(fin)
    val fmt = DateTimeFormatter.ofPattern("d MMM", Locale("es", "MX"))
    val yearFmt = DateTimeFormatter.ofPattern("yyyy", Locale("es", "MX"))
    val yearSuffix = if (start.year == end.year) " ${start.format(yearFmt)}" else ""
    return "${start.format(fmt)} – ${end.format(fmt)}$yearSuffix"
}

fun formatPrice(value: Double): String {
    return if (value % 1.0 == 0.0) value.toInt().toString() else String.format(Locale.US, "%.2f", value)
}
