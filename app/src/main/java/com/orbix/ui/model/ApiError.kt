package com.orbix.ui.model

data class ApiError(
    val error: String,
    val fields: Map<String, String>? = null
)
