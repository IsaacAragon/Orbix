package com.orbix.ui.navigation

import android.widget.Toast

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.orbix.ui.screen.CarDetailScreen
import com.orbix.ui.screen.CarReviewScreen
import com.orbix.ui.screen.FavoritesScreen
import com.orbix.ui.screen.LoginScreen
import com.orbix.ui.screen.MainScreen
import com.orbix.ui.screen.NewVehicleScreen
import com.orbix.ui.screen.SignUpScreen
import com.orbix.ui.screen.TermsAndConditionsScreen
import com.orbix.ui.screen.UserReviewScreen

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
                onNavigateToCarDetail = { carId ->
                    navController.navigate(CarDetail(carId))
                },
                onNavigateToNewVehicle = {
                    navController.navigate(NewVehicle)
                },
                onNavigateToTermsAndConditions = {
                    navController.navigate(TermsAndConditions(isSignUpFlow = false))
                },
                onNavigateToFavorites = {
                    navController.navigate(Favorites)
                },
                onNavigateToSignUp = {
                    navController.navigate(SignUp)
                }
            )
        }

        composable<CarDetail> { backStackEntry ->
            val carDetail: CarDetail = backStackEntry.toRoute()
            CarDetailScreen(
                carId = carDetail.carId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<NewVehicle> {
            NewVehicleScreen(
                onBack = { navController.popBackStack() },
                onVehicleAdded = { navController.popBackStack() }
            )
        }

        composable<TermsAndConditions> { backStackEntry ->
            val termsRoute: TermsAndConditions = backStackEntry.toRoute()
            val context = LocalContext.current
            TermsAndConditionsScreen(
                onBack = { navController.popBackStack() },
                onAccept = {
                    if (termsRoute.isSignUpFlow) {
                        Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()
                        navController.navigate(Home) {
                            popUpTo(SignUp) { inclusive = true }
                        }
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<SignUp> {
            SignUpScreen(
                onBack = { navController.popBackStack() },
                onNavigateToTerms = {
                    navController.navigate(TermsAndConditions(isSignUpFlow = true))
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

        composable<Favorites> {
            FavoritesScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

