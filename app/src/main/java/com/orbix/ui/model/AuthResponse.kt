package com.orbix.ui.model

data class AuthResponse(
    val token: String?,
    val type: String?,
    val email: String,
    val roles: List<String>,
    val permissions: List<String>,
    val userId: Long? = null,
    val nombre: String? = null
)
