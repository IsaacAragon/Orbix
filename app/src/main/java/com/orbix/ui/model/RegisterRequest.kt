package com.orbix.ui.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val nombre: String?,
    val telefono: String? = null,
    val fechaNacimiento: String? = null
)