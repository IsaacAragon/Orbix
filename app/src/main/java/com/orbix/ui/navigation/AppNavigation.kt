package com.orbix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orbix.ui.screen.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {

        composable<Login> {
            LoginScreen(
                onLogin = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Home> {
            MainScreen(
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(Home) { inclusive = true }
                        launchSingleTop = true
                    }
                },

                onNavigateToCarReview = {
                    navController.navigate(CarReview)
                },

                onNavigateToUserReview = {
                    navController.navigate(UserReview)
                },

                onNavigateToIDVerification = {
                    navController.navigate(IDVerification)
                },

                onNavigateToSearch = {
                    navController.navigate(Search)
                }
            )
        }

        composable<CarReview> {
            CarReviewScreen(
                onBack = { navController.popBackStack() },
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }

        composable<UserReview> {
            UserReviewScreen(
                onBack = { navController.popBackStack() },
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }

        composable<IDVerification> {
            IDVerificationScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Search> {
            SearchScreen(
                onBack = {
                    navController.popBackStack()
                },

                onVehicleSelected = {
                    // Más adelante:
                    // navController.navigate(RentalDetail)
                }
            )
        }
    }
}