package com.orbix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.orbix.ui.local.TokenStorage
import com.orbix.ui.navigation.AppNavigation
import com.orbix.ui.service.ApiClient
import com.orbix.ui.theme.OrbixTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ApiClient.init(applicationContext)
        runBlocking {
            TokenStorage(applicationContext).syncToApiClient()
        }

        setContent {
            OrbixTheme {
                AppNavigation()
            }
        }
    }
}
