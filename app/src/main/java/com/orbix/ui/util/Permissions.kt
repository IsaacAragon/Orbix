package com.orbix.ui.util

object Permissions {
    fun canCreateVehicle(permissions: Set<String>) =
        permissions.contains("vehicles:create")

    fun canReadVehicles(permissions: Set<String>) =
        permissions.contains("vehicles:read")

    fun canCreateRental(permissions: Set<String>) =
        permissions.contains("rentals:create")
}
