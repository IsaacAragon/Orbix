package com.orbix.ui.model

enum class VehicleCategory {
    ECONOMICO,
    SEDAN,
    SUV,
    PICKUP,
    DEPORTIVO,
    VAN,
    LUJO
}

fun VehicleCategory.label(): String = when (this) {
    VehicleCategory.ECONOMICO -> "Económico"
    VehicleCategory.SEDAN -> "Sedán"
    VehicleCategory.SUV -> "SUV"
    VehicleCategory.PICKUP -> "Pickup"
    VehicleCategory.DEPORTIVO -> "Deportivo"
    VehicleCategory.VAN -> "Van"
    VehicleCategory.LUJO -> "Lujo"
}

fun categoryLabel(value: String?): String {
    if (value.isNullOrBlank()) return "—"
    return runCatching { VehicleCategory.valueOf(value).label() }.getOrElse { value }
}

fun List<Vehicle>.byCategory(category: VehicleCategory?): List<Vehicle> =
    if (category == null) this else filter { it.category == category.name }
