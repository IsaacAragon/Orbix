package com.orbix.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orbix.ui.screen.HomeScreen
import com.orbix.ui.screen.LoginScreen

@Composable
fun AppNavigation(modifier: Modifier) {
    val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Login)
        {
            composable<Login> {
                LoginScreen(
                    onLogin = {
                        navController.navigate(Home) {
                        }
                    }
                )
            }
            composable<Home> {
                HomeScreen(
                    onLogout = {
                        navController.popBackStack()
                    }
                )
            }
        }

}