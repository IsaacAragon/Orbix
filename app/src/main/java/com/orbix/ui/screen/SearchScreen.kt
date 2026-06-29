package com.orbix.ui.screen

import android.service.autofill.OnClickAction
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orbix.ui.model.Vehicle
import com.orbix.ui.model.VehicleCategory
import com.orbix.ui.model.label
import com.orbix.ui.viewmodel.VehicleViewModel
import kotlinx.coroutines.delay
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onVehicleSelected: (String) -> Unit,
    vm: VehicleViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {
    var searchText by remember { mutableStateOf("") }
    var debouncedSearchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }

    // Debounce search input to minimize calculations and UI jitter
    LaunchedEffect(searchText) {
        delay(300)
        debouncedSearchText = searchText
    }

    LaunchedEffect(Unit) {
        vm.loadVehicles()
    }

    val categories = remember {
        listOf("Todos") + VehicleCategory.entries.map { it.label() }
    }

    val filteredVehicles = remember(debouncedSearchText, selectedCategory, vm.vehicles) {
        vm.vehicles.filter { vehicle ->
            // Only show available vehicles as requested
            val matchesAvailability = vehicle.isAvailable
            
            // Name match (brand + model) case-insensitive
            val name = "${vehicle.brand} ${vehicle.model}"
            val matchesSearch = debouncedSearchText.isBlank() || name.contains(debouncedSearchText, ignoreCase = true)
            
            // Category match
            val matchesCategory = if (selectedCategory == "Todos") {
                true
            } else {
                val enumCategory = VehicleCategory.entries.find { it.label() == selectedCategory }
                vehicle.category.orEmpty().equals(enumCategory?.name, ignoreCase = true) ||
                        vehicle.category.orEmpty().equals(selectedCategory, ignoreCase = true)
            }
            
            matchesAvailability && matchesSearch && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Buscar Vehículos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Encuentra tu vehículo ideal",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Explora y filtra vehículos disponibles.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Buscar por marca o modelo")
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Tune,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true
                )
            }

            item {
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                            },
                            label = {
                                Text(category)
                            }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Resultados disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (vm.isRefreshing && vm.vehicles.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (filteredVehicles.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron vehículos disponibles.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filteredVehicles) { vehicle ->
                    VehicleCard(
                        vehicle = vehicle,
                        onNavigateToCarDetail = onVehicleSelected,
                        onToggleFavorite = { vm.toggleFavorite(it) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}