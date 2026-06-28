package com.orbix.ui.util

import com.google.gson.Gson
import com.orbix.ui.model.ApiError
import retrofit2.HttpException

private val gson = Gson()

fun parseApiError(e: HttpException, fallback: String = "Error del servidor"): String {
    return try {
        val body = e.response()?.errorBody()?.charStream() ?: return fallback
        val apiError = gson.fromJson(body, ApiError::class.java)
        apiError.error.ifBlank { fallback }
    } catch (_: Exception) {
        fallback
    }
}
