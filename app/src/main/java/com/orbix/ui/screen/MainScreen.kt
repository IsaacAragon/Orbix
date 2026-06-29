package com.orbix.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.orbix.ui.navigation.NavItem
import com.orbix.ui.util.Roles
import com.orbix.ui.viewmodel.VehicleViewModel

@Composable
fun MainScreen(
    userEmail: String,
    userPermissions: Set<String>,
    userRoles: Set<String>,
    onLogout: () -> Unit,
    onNavigateToCarReview: (Long) -> Unit,
    onNavigateToUserReview: (Long) -> Unit,
    onNavigateToCarDetail: (String) -> Unit,
    onNavigateToNewVehicle: () -> Unit,
    onNavigateToTermsAndConditions: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToIDVerification: () -> Unit,
    onNavigateToCarManagement: () -> Unit,
    onNavigateToRentalManagement: () -> Unit,
    onNavigateToReviewSelection: () -> Unit
) {
    val activity = LocalContext.current as ComponentActivity
    val vehicleViewModel: VehicleViewModel = viewModel(viewModelStoreOwner = activity)

    val isArrendador = Roles.isArrendador(userRoles)
    val navItems = listOf(
        NavItem("Inicio", Icons.Default.Home, 0),
        NavItem(if (isArrendador) "Solicitudes" else "Reservas", Icons.Default.DateRange, 1),
        NavItem("Perfil", Icons.Default.Person, 2)
    )

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 0.dp
            ) {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedItem == item.index,
                        onClick = {
                            if (selectedItem == 0 && item.index == 0) {
                                vehicleViewModel.loadVehicles()
                            } else {
                                selectedItem = item.index
                            }
                        },
                        label = { 
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedItem == item.index) FontWeight.Bold else FontWeight.Normal
                            ) 
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> HomeScreen(
                    userPermissions = userPermissions,
                    userRoles = userRoles,
                    onNavigateToCarDetail = onNavigateToCarDetail,
                    onNavigateToNewVehicle = onNavigateToNewVehicle,
                    onNavigateToSearch = onNavigateToSearch,
                    onNavigateToFavorites = onNavigateToFavorites
                )
                1 -> {
                    if (isArrendador) {
                        RentalManagementScreen(
                            onBack = { selectedItem = 0 }
                        )
                    } else {
                        ReservationsScreen(
                            userRoles = userRoles,
                            onBack = { selectedItem = 0 },
                            onNavigateToCarReview = onNavigateToCarReview
                        )
                    }
                }
                2 -> ProfileScreen(
                    userEmail = userEmail,
                    userRoles = userRoles,
                    onLogout = onLogout,
                    onNavigateToTermsAndConditions = onNavigateToTermsAndConditions,
                    onNavigateToCarReview = {
                        vehicleViewModel.vehicles.firstOrNull()?.id?.let(onNavigateToCarReview)
                    },
                    onNavigateToUserReview = { onNavigateToUserReview(2L) },
                    onNavigateToFavorites = onNavigateToFavorites,
                    onNavigateToSignUp = onNavigateToSignUp,
                    onNavigateToIDVerification = onNavigateToIDVerification,
                    onNavigateToCarManagement = onNavigateToCarManagement,
                    onNavigateToRentalManagement = onNavigateToRentalManagement,
                    onNavigateToReviewSelection = onNavigateToReviewSelection
                )
            }
        }
    }
}

