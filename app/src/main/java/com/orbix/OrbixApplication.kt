package com.orbix

import android.app.Application
import com.orbix.ui.local.TokenStorage
import com.orbix.ui.repository.AuthRepository
import com.orbix.ui.service.ApiClient
import kotlinx.coroutines.runBlocking

class OrbixApplication : Application() {

    val tokenStorage: TokenStorage by lazy { TokenStorage(this) }
    val authRepository: AuthRepository by lazy { AuthRepository(tokenStorage) }

    override fun onCreate() {
        super.onCreate()
        ApiClient.init(this)
        runBlocking { tokenStorage.syncToApiClient() }
    }
}
