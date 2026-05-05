package com.orbix.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.orbix.ui.navigation.NavItem

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onNavigateToCarReview: () -> Unit,
    onNavigateToUserReview: () -> Unit
) {
    val navItems = listOf(
        NavItem("Inicio", Icons.Default.Home, 0),
        NavItem("Reseñas", Icons.Default.RateReview, 1),
        NavItem("Perfil", Icons.Default.Person, 2),
        NavItem("Salir", Icons.Default.Logout, 3)
    )

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedItem == item.index,
                        onClick = {
                            if (item.index == 3) {
                                onLogout()
                            } else {
                                selectedItem = item.index
                            }
                        },
                        label = { Text(text = item.label) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
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
                0 -> HomeScreen()
                1 -> ReviewSelectionScreen(
                    onNavigateToCarReview = onNavigateToCarReview,
                    onNavigateToUserReview = onNavigateToUserReview
                )
                2 -> ProfileScreen()
            }
        }
    }
}
