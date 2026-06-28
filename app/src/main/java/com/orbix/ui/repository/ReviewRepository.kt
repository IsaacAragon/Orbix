package com.orbix.ui.repository

import com.orbix.ui.model.AllReviewTagsResponse
import com.orbix.ui.model.CreateUserReviewRequest
import com.orbix.ui.model.CreateVehicleReviewRequest
import com.orbix.ui.model.UserReviewResponse
import com.orbix.ui.model.UserReviewSummary
import com.orbix.ui.model.VehicleReviewResponse
import com.orbix.ui.model.VehicleReviewSummary
import com.orbix.ui.service.ApiClient
import com.orbix.ui.service.ApiResult
import retrofit2.HttpException

class ReviewRepository {
    suspend fun getVehicleReviews(vehicleId: Long): ApiResult<List<VehicleReviewResponse>> {
        return try {
            ApiResult.Success(ApiClient.reviewApi.getVehicleReviews(vehicleId))
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar reseñas")
        }
    }

    suspend fun getVehicleSummary(vehicleId: Long): ApiResult<VehicleReviewSummary> {
        return try {
            ApiResult.Success(ApiClient.reviewApi.getVehicleSummary(vehicleId))
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar resumen")
        }
    }

    suspend fun createVehicleReview(
        vehicleId: Long,
        rating: Int,
        tags: List<String>,
        comment: String?
    ): ApiResult<VehicleReviewResponse> {
        return try {
            ApiResult.Success(
                ApiClient.reviewApi.createVehicleReview(
                    CreateVehicleReviewRequest(vehicleId, rating, tags, comment?.ifBlank { null })
                )
            )
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error("No tienes permiso para reseñar este vehículo.")
                400 -> ApiResult.Error("Datos inválidos o ya reseñaste este vehículo.")
                else -> ApiResult.Error("Error del servidor")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    suspend fun getUserReviews(userId: Long): ApiResult<List<UserReviewResponse>> {
        return try {
            ApiResult.Success(ApiClient.reviewApi.getUserReviews(userId))
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar reseñas")
        }
    }

    suspend fun getUserSummary(userId: Long): ApiResult<UserReviewSummary> {
        return try {
            ApiResult.Success(ApiClient.reviewApi.getUserSummary(userId))
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar resumen")
        }
    }

    suspend fun createUserReview(
        reviewedUserId: Long,
        rating: Int,
        tags: List<String>,
        comment: String?
    ): ApiResult<UserReviewResponse> {
        return try {
            ApiResult.Success(
                ApiClient.reviewApi.createUserReview(
                    CreateUserReviewRequest(reviewedUserId, rating, tags, comment?.ifBlank { null })
                )
            )
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiResult.Error("Sesión expirada. Inicia sesión de nuevo.")
                403 -> ApiResult.Error("No tienes permiso para reseñar a este usuario.")
                400 -> ApiResult.Error("Datos inválidos o ya reseñaste a este usuario.")
                else -> ApiResult.Error("Error del servidor")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    suspend fun getAllReviewTags(): ApiResult<AllReviewTagsResponse> {
        return try {
            ApiResult.Success(ApiClient.reviewApi.getAllReviewTags())
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error al cargar tags de reseña")
        }
    }
}
