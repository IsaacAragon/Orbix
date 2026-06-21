package com.orbix.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarManagementScreen(
    onBack: () -> Unit
) {
    //DEMO, YA IMPLEMENTADA LA API SE VA CAMBIAR
    val cars = listOf(
        ManagedCar("1", "Toyota Yaris", 25.0, true),
        ManagedCar("2", "Suzuki Swift", 22.0, true),
        ManagedCar("3", "Hyundai Tucson", 40.0, false)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Mis Vehiculos", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(cars) { car ->

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        // 🚗 INFO PRINCIPAL
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                Text(
                                    car.name,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    "$${car.price} / día",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Estado
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Icon(
                                    imageVector = if (car.isActive)
                                        Icons.Default.CheckCircle
                                    else
                                        Icons.Default.Block,
                                    contentDescription = null,
                                    tint = if (car.isActive)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.error
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = if (car.isActive) "Activo" else "Bloqueado",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Button(
                                onClick = { /* TODO EDIT (API FUTURO) */ },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Editar")
                            }

                            OutlinedButton(
                                onClick = {
                                    //MOCK TOGGLE STATUS (API FUTURO)
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Block, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Bloquear")
                            }
                        }
                    }
                }
            }
        }
    }
}