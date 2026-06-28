package com.orbix.ui.util

object Roles {
    fun canReviewVehicle(roles: Set<String>) =
        roles.contains("ROLE_CLIENTE")

    fun canReviewUser(roles: Set<String>) =
        roles.contains("ROLE_ARRENDADOR")
}
