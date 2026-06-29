package com.orbix.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.orbix.ui.repository.UserSession
import com.orbix.ui.screen.CarDetailScreen
import com.orbix.ui.screen.CarManagementScreen
import com.orbix.ui.screen.CarReviewScreen
import com.orbix.ui.screen.FavoritesScreen
import com.orbix.ui.screen.HostDashboardScreen
import com.orbix.ui.screen.HostDetailScreen
import com.orbix.ui.screen.HostRulesScreen
import com.orbix.ui.screen.IDVerificationScreen
import com.orbix.ui.screen.MainScreen
import com.orbix.ui.screen.NewVehicleScreen
import com.orbix.ui.screen.RentalDetailScreen
import com.orbix.ui.screen.RentalManagementScreen
import com.orbix.ui.screen.ReviewSelectionScreen
import com.orbix.ui.screen.SearchScreen
import com.orbix.ui.screen.TermsAndConditionsScreen
import com.orbix.ui.screen.UserReviewScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orbix.ui.viewmodel.RentalViewModel
import com.orbix.ui.viewmodel.ReviewViewModel
import com.orbix.ui.viewmodel.SessionViewModel

fun NavGraphBuilder.authenticatedRoutes(
    navController: NavHostController,
    session: UserSession,
    sessionViewModel: SessionViewModel
) {
    composable<HomeCliente> {
        AuthenticatedMainScreen(session, sessionViewModel, navController)
    }

    composable<HomeArrendador> {
        AuthenticatedMainScreen(session, sessionViewModel, navController)
    }

    composable<CarDetail> { backStackEntry ->
        val carDetail: CarDetail = backStackEntry.toRoute()
        CarDetailScreen(
            carId = carDetail.carId,
            userRoles = session.roles,
            userPermissions = session.permissions,
            onBack = { navController.popBackStack() },
            onNavigateToRules = { navController.navigate(HostRules) },
            onNavigateToReview = { vehicleId ->
                navController.navigate(CarReview(vehicleId))
            }
        )
    }

    composable<NewVehicle> {
        val context = LocalContext.current
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

    composable<CarReview> { backStackEntry ->
        val route: CarReview = backStackEntry.toRoute()
        val activity = LocalContext.current as? androidx.activity.ComponentActivity
        val reviewViewModel: ReviewViewModel? = activity?.let { viewModel(viewModelStoreOwner = it) }
        CarReviewScreen(
            vehicleId = route.vehicleId,
            onBack = { navController.popBackStack() },
            onReviewSubmitted = {
                reviewViewModel?.loadVehicleReviews(route.vehicleId)
                navController.popBackStack()
            },
            viewModel = reviewViewModel ?: viewModel()
        )
    }

    composable<UserReview> { backStackEntry ->
        val route: UserReview = backStackEntry.toRoute()
        val activity = LocalContext.current as? androidx.activity.ComponentActivity
        val rentalViewModel: RentalViewModel? = activity?.let { viewModel(viewModelStoreOwner = it) }
        UserReviewScreen(
            reviewedUserId = route.reviewedUserId,
            targetName = route.targetName,
            onBack = { navController.popBackStack() },
            onReviewSubmitted = {
                rentalViewModel?.loadReceivedRentals()
                navController.popBackStack()
            }
        )
    }

    composable<Favorites> {
        FavoritesScreen(
            onBack = { navController.popBackStack() },
            onNavigateToCarDetail = { carId ->
                navController.navigate(CarDetail(carId))
            }
        )
    }

    composable<IDVerification> {
        IDVerificationScreen(onBack = { navController.popBackStack() })
    }

    composable<Search> {
        SearchScreen(
            onBack = { navController.popBackStack() },
            onVehicleSelected = { navController.navigate(RentalDetail) }
        )
    }

    composable<RentalDetail> {
        RentalDetailScreen(
            onBack = { navController.popBackStack() },
            onNavigateToCarDetail = { navController.navigate(CarDetail("1")) },
            onNavigateToHostDetail = { navController.navigate(HostDetail) }
        )
    }

    composable<HostDetail> {
        HostDetailScreen(
            onBack = { navController.popBackStack() },
            onNavigateToDashboard = { navController.navigate(HostDashboard) }
        )
    }

    composable<HostDashboard> {
        HostDashboardScreen(
            onBack = { navController.popBackStack() },
            onNavigateToCarManagement = { navController.navigate(CarManagement) },
            onNavigateToRentalManagement = { navController.navigate(RentalManagement) }
        )
    }

    composable<CarManagement> {
        CarManagementScreen(onBack = { navController.popBackStack() })
    }

    composable<RentalManagement> {
        RentalManagementScreen(onBack = { navController.popBackStack() })
    }

    composable<HostRules> {
        HostRulesScreen(onBack = { navController.popBackStack() })
    }

    composable<ReviewSelection> {
        ReviewSelectionScreen(
            onNavigateToCarReview = { navController.navigate(CarReview(2L)) },
            onNavigateToUserReview = { navController.navigate(UserReview(2L, null)) }
        )
    }
}

@Composable
private fun AuthenticatedMainScreen(
    session: UserSession,
    sessionViewModel: SessionViewModel,
    navController: NavHostController
) {
    MainScreen(
        userEmail = session.email,
        userPermissions = session.permissions,
        userRoles = session.roles,
        userNombre = session.nombre,
        userTelefono = session.telefono,
        onPhoneUpdated = { telefono -> sessionViewModel.patchSessionTelefono(telefono) },
        onLogout = { sessionViewModel.logout() },
        onNavigateToCarReview = { vehicleId ->
            navController.navigate(CarReview(vehicleId))
        },
        onNavigateToUserReview = { userId, targetName ->
            navController.navigate(UserReview(userId, targetName))
        },
        onNavigateToCarDetail = { carId ->
            navController.navigate(CarDetail(carId))
        },
        onNavigateToNewVehicle = { navController.navigate(NewVehicle) },
        onNavigateToTermsAndConditions = {
            navController.navigate(TermsAndConditions(isSignUpFlow = false))
        },
        onNavigateToFavorites = { navController.navigate(Favorites) },
        onNavigateToIDVerification = { navController.navigate(IDVerification) },
        onNavigateToSearch = { navController.navigate(Search) },
        onNavigateToCarManagement = { navController.navigate(CarManagement) },
        onNavigateToRentalManagement = { navController.navigate(RentalManagement) },
        onNavigateToReviewSelection = { navController.navigate(ReviewSelection) }
    )
}
