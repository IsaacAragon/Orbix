package com.orbix.ui.util

object Roles {
    fun canReviewVehicle(roles: Set<String>) =
        roles.contains("ROLE_CLIENTE")

    fun canReviewUser(roles: Set<String>) =
        roles.contains("ROLE_ARRENDADOR")

    fun isCliente(roles: Set<String>) =
        roles.contains("ROLE_CLIENTE")

    fun isArrendador(roles: Set<String>) =
        roles.contains("ROLE_ARRENDADOR")

    fun isAdmin(roles: Set<String>) =
        roles.contains("ROLE_ADMIN")
}
