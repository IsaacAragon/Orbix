package com.orbix.ui.util

import android.util.Patterns
import java.util.Calendar
import java.util.Locale

fun validateRegister(
    email: String,
    password: String,
    confirmPassword: String,
    fechaNacimiento: String?
): String? {
    if (email.isBlank()) return "El correo es obligatorio"
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return "El correo no es válido"
    }
    if (password.length < 6) {
        return "La contraseña debe tener al menos 6 caracteres y contener al menos una letra"
    }
    if (!password.any { it.isLetter() }) {
        return "La contraseña debe tener al menos 6 caracteres y contener al menos una letra"
    }
    if (password != confirmPassword) {
        return "Las contraseñas no coinciden"
    }
    validateBirthDate(fechaNacimiento)?.let { return it }
    return null
}

fun validateBirthDate(fechaNacimiento: String?): String? {
    if (fechaNacimiento.isNullOrBlank()) {
        return "La fecha de nacimiento es obligatoria"
    }
    if (!isAtLeast18YearsOld(fechaNacimiento)) {
        return "Debes ser mayor de 18 años para registrarte"
    }
    return null
}

fun isAtLeast18YearsOld(isoDate: String): Boolean {
    val parts = isoDate.split("-")
    if (parts.size != 3) return false

    val birth = Calendar.getInstance().apply {
        set(Calendar.YEAR, parts[0].toInt())
        set(Calendar.MONTH, parts[1].toInt() - 1)
        set(Calendar.DAY_OF_MONTH, parts[2].toInt())
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val minimumBirthDate = Calendar.getInstance().apply {
        add(Calendar.YEAR, -18)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return !birth.after(minimumBirthDate)
}

fun toApiDate(day: Int, month: Int, year: Int): String {
    return String.format(Locale.US, "%04d-%02d-%02d", year, month, day)
}

fun minimumBirthDateCalendar(): Calendar = Calendar.getInstance().apply {
    add(Calendar.YEAR, -18)
}
