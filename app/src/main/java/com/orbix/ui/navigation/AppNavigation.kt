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
import com.orbix.ui.screen.RentalDetailScreen
import com.orbix.ui.screen.HostDetailScreen
import com.orbix.ui.screen.HostDashboardScreen
import com.orbix.ui.screen.CarManagementScreen
import com.orbix.ui.screen.HostRulesScreen
import com.orbix.ui.screen.RentalManagementScreen
import com.orbix.ui.screen.ReviewSelectionScreen
import com.orbix.ui.viewmodel.SessionState
import com.orbix.ui.viewmodel.SessionViewModel

@Composable
fun AppNavigation() {
    val sessionViewModel: SessionViewModel = viewModel()
    val navController = rememberNavController()
    val context = LocalContext.current

    val onRegisterSuccess: () -> Unit = {
        sessionViewModel.restoreSession()
        Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()
    }

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
                        onLogin = { response ->
                            sessionViewModel.onLoginSuccessFromResponse(response)
                        },
                        onNavigateToSignUp = { navController.navigate(SignUp) }
                    )
                }

                composable<SignUp> {
                    SignUpScreen(
                        onBack = { navController.popBackStack() },
                        onRegisterSuccess = onRegisterSuccess
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
                        userRoles = state.session.roles,
                        onLogout = { sessionViewModel.logout() },
                        onNavigateToCarReview = { vehicleId ->
                            navController.navigate(CarReview(vehicleId))
                        },
                        onNavigateToUserReview = { userId ->
                            navController.navigate(UserReview(userId))
                        },
                        onNavigateToCarDetail = { carId ->
                            navController.navigate(CarDetail(carId))
                        },
                        onNavigateToNewVehicle = { navController.navigate(NewVehicle) },
                        onNavigateToTermsAndConditions = {
                            navController.navigate(TermsAndConditions(isSignUpFlow = false))
                        },
                        onNavigateToFavorites = { navController.navigate(Favorites) },
                        onNavigateToSignUp = { navController.navigate(SignUp) },
                        onNavigateToIDVerification = { navController.navigate(IDVerification) },
                        onNavigateToSearch = { navController.navigate(Search) },
                        onNavigateToCarManagement = { navController.navigate(CarManagement) },
                        onNavigateToRentalManagement = { navController.navigate(RentalManagement) },
                        onNavigateToReviewSelection = { navController.navigate(ReviewSelection) }
                    )
                }

                composable<CarDetail> { backStackEntry ->
                    val carDetail: CarDetail = backStackEntry.toRoute()
                    CarDetailScreen(
                        carId = carDetail.carId,
                        userRoles = state.session.roles,
                        onBack = { navController.popBackStack() },
                        onNavigateToRules = {
                            navController.navigate(HostRules)
                        },
                        onNavigateToReview = { vehicleId ->
                            navController.navigate(CarReview(vehicleId))
                        }
                    )
                }

                composable<NewVehicle> {
                    NewVehicleScreen(
                        onBack = { navController.popBackStack() },
                        onVehicleAdded = {
                            Toast.makeText(context, "Vehículo publicado", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    )
                }

                composable<TermsAndConditions> { backStackEntry ->
                    val termsRoute: TermsAndConditions = backStackEntry.toRoute()
                    TermsAndConditionsScreen(
                        onBack = { navController.popBackStack() },
                        onAccept = {
                            if (!termsRoute.isSignUpFlow) {
                                navController.popBackStack()
                            }
                        }
                    )
                }

                composable<SignUp> {
                    SignUpScreen(
                        onBack = { navController.popBackStack() },
                        onRegisterSuccess = {
                            onRegisterSuccess()
                            navController.popBackStack()
                        }
                    )
                }

                composable<CarReview> { backStackEntry ->
                    val route: CarReview = backStackEntry.toRoute()
                    CarReviewScreen(
                        vehicleId = route.vehicleId,
                        onBack = { navController.popBackStack() },
                        onReviewSubmitted = { navController.popBackStack() }
                    )
                }

                composable<UserReview> { backStackEntry ->
                    val route: UserReview = backStackEntry.toRoute()
                    UserReviewScreen(
                        reviewedUserId = route.reviewedUserId,
                        onBack = { navController.popBackStack() },
                        onReviewSubmitted = { navController.popBackStack() }
                    )
                }

                composable<Favorites> {
                    FavoritesScreen(onBack = { navController.popBackStack() })
                }

                composable<IDVerification> {
                    IDVerificationScreen(onBack = { navController.popBackStack() })
                }

                composable<Search> {
                    SearchScreen(
                        onBack = { navController.popBackStack() },
                        onVehicleSelected = {
                            navController.navigate(RentalDetail)
                        }
                    )
                }

                composable<RentalDetail> {
                    RentalDetailScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToCarDetail = {
                            navController.navigate(CarDetail("1"))
                        },
                        onNavigateToHostDetail = {
                            navController.navigate(HostDetail)
                        }
                    )
                }

                composable<HostDetail> {
                    HostDetailScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToDashboard = {
                            navController.navigate(HostDashboard)
                        }
                    )
                }

                composable<HostDashboard> {
                    HostDashboardScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToCarManagement = {
                            navController.navigate(CarManagement)
                        },
                        onNavigateToRentalManagement = {
                            navController.navigate(RentalManagement)
                        }
                    )
                }

                composable<CarManagement> {
                    CarManagementScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<RentalManagement> {
                    RentalManagementScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<HostRules> {
                    HostRulesScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<ReviewSelection> {
                    ReviewSelectionScreen(
                        onNavigateToCarReview = {
                            navController.navigate(CarReview(2L))
                        },
                        onNavigateToUserReview = {
                            navController.navigate(UserReview(2L))
                        }
                    )
                }
            }
        }
    }
}
