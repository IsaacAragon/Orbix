package com.orbix.ui.util

import com.orbix.ui.navigation.HomeArrendador
import com.orbix.ui.navigation.HomeCliente

object AuthNavigation {

    fun homeRoute(roles: Collection<String>) = when {
        roles.contains("ROLE_ARRENDADOR") -> HomeArrendador
        roles.contains("ROLE_CLIENTE") -> HomeCliente
        else -> HomeCliente
    }

    fun authSuccessMessage(roles: Collection<String>, isRegistration: Boolean): String {
        val roleHint = when {
            roles.contains("ROLE_ARRENDADOR") -> " Cuenta de arrendador."
            roles.contains("ROLE_CLIENTE") -> " Cuenta de cliente."
            else -> ""
        }
        return if (isRegistration) {
            "¡Cuenta creada con éxito!$roleHint"
        } else {
            "¡Bienvenido!$roleHint"
        }
    }
}
