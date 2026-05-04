package com.orbix.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orbix.ui.screen.CarReviewScreen
import com.orbix.ui.screen.HomeScreen
import com.orbix.ui.screen.LoginScreen
import com.orbix.ui.screen.UserReviewScreen

@Composable
fun AppNavigation() {
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
                },
                onNavigateToCarReview = {
                    navController.navigate(CarReview)
                },
                onNavigateToUserReview = {
                    navController.navigate(UserReview)
                }

            )
        }
        composable<CarReview> {
            CarReviewScreen(
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }
        composable<UserReview> {
            UserReviewScreen(
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }
    }

}