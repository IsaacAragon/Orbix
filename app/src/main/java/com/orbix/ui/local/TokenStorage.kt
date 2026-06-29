package com.orbix.ui.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
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
            response.token?.takeIf { it.isNotBlank() }?.let { prefs[KEY_TOKEN] = it }
            prefs[KEY_EMAIL] = response.email
            prefs[KEY_ROLES] = response.roles.toSet()
            prefs[KEY_PERMISSIONS] = response.permissions.toSet()
            response.userId?.let { prefs[KEY_USER_ID] = it }
            response.nombre?.takeIf { it.isNotBlank() }?.let { prefs[KEY_NOMBRE] = it }
            response.telefono?.takeIf { it.isNotBlank() }?.let { prefs[KEY_TELEFONO] = it }
        }
        response.token?.takeIf { it.isNotBlank() }?.let { ApiClient.setToken(it) }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
        }
        ApiClient.setToken(token)
    }

    suspend fun getToken(): String? {
        val token = context.dataStore.data.map { it[KEY_TOKEN] }.first()
        return token?.takeIf { it.isNotBlank() }
    }

    suspend fun syncToApiClient() {
        getToken()?.let { ApiClient.setToken(it) }
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

    suspend fun getUserId(): Long? {
        return context.dataStore.data.map { it[KEY_USER_ID] }.first()
    }

    suspend fun saveTelefono(telefono: String?) {
        context.dataStore.edit { prefs ->
            if (telefono.isNullOrBlank()) {
                prefs.remove(KEY_TELEFONO)
            } else {
                prefs[KEY_TELEFONO] = telefono
            }
        }
    }

    suspend fun getNombre(): String? {
        return context.dataStore.data.map { it[KEY_NOMBRE] }.first()
    }

    suspend fun getTelefono(): String? {
        return context.dataStore.data.map { it[KEY_TELEFONO] }.first()
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
        ApiClient.clearToken()
    }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_ROLES = stringSetPreferencesKey("roles")
        private val KEY_PERMISSIONS = stringSetPreferencesKey("permissions")
        private val KEY_USER_ID = longPreferencesKey("user_id")
        private val KEY_NOMBRE = stringPreferencesKey("nombre")
        private val KEY_TELEFONO = stringPreferencesKey("telefono")
    }
}
