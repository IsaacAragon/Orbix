package com.orbix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orbix.ui.screen.CarReviewScreen
import com.orbix.ui.screen.HomeScreen
import com.orbix.ui.screen.LoginScreen
import com.orbix.ui.screen.ReservationsScreen
import com.orbix.ui.screen.UserReviewScreen

// ✅ RUTAS (SIN CONFLICTOS)
const val ROUTE_LOGIN = "login"
const val ROUTE_HOME = "home"
const val ROUTE_CAR_REVIEW = "car_review"
const val ROUTE_USER_REVIEW = "user_review"
const val ROUTE_RESERVATIONS = "reservations"

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_LOGIN
    ) {

        // 🔐 LOGIN
        composable(ROUTE_LOGIN) {
            LoginScreen(
                onLogin = {
                    navController.navigate(ROUTE_HOME) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🏠 HOME
        composable(ROUTE_HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToCarReview = {
                    navController.navigate(ROUTE_CAR_REVIEW)
                },
                onNavigateToUserReview = {
                    navController.navigate(ROUTE_USER_REVIEW)
                },
                onNavigateToReservations = {
                    navController.navigate(ROUTE_RESERVATIONS)
                }
            )
        }

        // 🚗 CAR REVIEW
        composable(ROUTE_CAR_REVIEW) {
            CarReviewScreen(
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }

        // 👤 USER REVIEW
        composable(ROUTE_USER_REVIEW) {
            UserReviewScreen(
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }

        // 📅 RESERVAS
        composable(ROUTE_RESERVATIONS) {
            ReservationsScreen(
                onBack = {
                    navController.popBackStack()
                },
                onRateService = {
                    navController.navigate(ROUTE_USER_REVIEW)
                }
            )
        }
    }
}