package com.orbix.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.orbix.ui.screen.CarDetailScreen
import com.orbix.ui.screen.CarReviewScreen
import com.orbix.ui.screen.FavoritesScreen
import com.orbix.ui.screen.IDVerificationScreen
import com.orbix.ui.screen.LoginScreen
import com.orbix.ui.screen.MainScreen
import com.orbix.ui.screen.NewVehicleScreen
import com.orbix.ui.screen.SearchScreen
import com.orbix.ui.screen.SignUpScreen
import com.orbix.ui.screen.TermsAndConditionsScreen
import com.orbix.ui.screen.UserReviewScreen
import com.orbix.ui.viewmodel.SessionState
import com.orbix.ui.viewmodel.SessionViewModel

@Composable
fun AppNavigation() {
    val sessionViewModel: SessionViewModel = viewModel()
    val navController = rememberNavController()

    when (val state = sessionViewModel.sessionState) {
        SessionState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        SessionState.Unauthenticated -> {
            NavHost(
                navController = navController,
                startDestination = Login
            ) {
                composable<Login> {
                    LoginScreen(
                        onLogin = {
                            sessionViewModel.restoreSession()
                        }
                    )
                }
            }
        }

        is SessionState.Authenticated -> {
            NavHost(
                navController = navController,
                startDestination = Home
            ) {
                composable<Home> {
                    MainScreen(
                        userEmail = state.session.email,
                        userPermissions = state.session.permissions,
                        onLogout = {
                            sessionViewModel.logout()
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
                        },
                        onNavigateToIDVerification = {
                            navController.navigate(IDVerification)
                        },
                        onNavigateToSearch = {
                            navController.navigate(Search)
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
                                Toast.makeText(
                                    context,
                                    "¡Cuenta creada con éxito!",
                                    Toast.LENGTH_LONG
                                ).show()
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
                        onVehicleSelected = { }
                    )
                }
            }
        }
    }
}
