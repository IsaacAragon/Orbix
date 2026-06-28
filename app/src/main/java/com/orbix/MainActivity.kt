package com.orbix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.orbix.ui.local.TokenStorage
import com.orbix.ui.navigation.AppNavigation
import com.orbix.ui.service.ApiClient
import com.orbix.ui.theme.OrbixTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ApiClient.init(applicationContext)
        lifecycleScope.launch {
            TokenStorage(applicationContext).syncToApiClient()
        }

        setContent {
            OrbixTheme {
                AppNavigation()
            }
        }
    }
}
