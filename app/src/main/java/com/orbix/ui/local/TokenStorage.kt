package com.orbix.ui.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.orbix.ui.model.AuthResponse
import com.orbix.ui.service.ApiClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_session")

class TokenStorage(private val context: Context) {

    suspend fun saveSession(response: AuthResponse) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = response.token.orEmpty()
            prefs[KEY_EMAIL] = response.email
            prefs[KEY_ROLES] = response.roles.toSet()
            prefs[KEY_PERMISSIONS] = response.permissions.toSet()
        }
        ApiClient.setToken(response.token)
    }

    suspend fun getToken(): String? {
        val token = context.dataStore.data.map { it[KEY_TOKEN] }.first()
        return token?.takeIf { it.isNotBlank() }
    }

    suspend fun getEmail(): String? {
        return context.dataStore.data.map { it[KEY_EMAIL] }.first()
    }

    suspend fun getPermissions(): Set<String> {
        return context.dataStore.data.map { it[KEY_PERMISSIONS] ?: emptySet() }.first()
    }

    suspend fun getRoles(): Set<String> {
        return context.dataStore.data.map { it[KEY_ROLES] ?: emptySet() }.first()
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
        ApiClient.setToken(null)
    }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_ROLES = stringSetPreferencesKey("roles")
        private val KEY_PERMISSIONS = stringSetPreferencesKey("permissions")
    }
}
