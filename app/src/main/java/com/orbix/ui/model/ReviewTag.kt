package com.orbix.ui.model

data class ReviewTagOption(
    val code: String,
    val label: String
)

data class AllReviewTagsResponse(
    val vehicle: Map<Int, List<ReviewTagOption>>,
    val user: Map<Int, List<ReviewTagOption>>,
    val vehicleTitles: Map<Int, String>,
    val userTitles: Map<Int, String>
)

data class ReviewTagsResponse(
    val rating: Int,
    val type: String,
    val title: String,
    val tags: List<ReviewTagOption>
)

fun formatTagCode(code: String): String = code
    .lowercase()
    .split('_')
    .joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
