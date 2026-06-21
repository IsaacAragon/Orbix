package com.orbix.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalManagementScreen(
    onBack: () -> Unit
) {

    // 🔶 MOCK DATA (REEMPLAZAR POR API)
    val rentals = listOf(
        ManagedRental("1", "Toyota Yaris", "Carlos", "20 Jun", "23 Jun", RentalStatus.PENDING),
        ManagedRental("2", "Hyundai Tucson", "Sofia", "18 Jun", "21 Jun", RentalStatus.ACTIVE),
        ManagedRental("3", "Suzuki Swift", "Isaac", "10 Jun", "12 Jun", RentalStatus.COMPLETED)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Gestion de Reservas", fontWeight = FontWeight.Bold)
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

            items(rentals) { rental ->

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        // 🚗 INFO
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(
                                    rental.carName,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Usuario: ${rental.userName}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "${rental.startDate} → ${rental.endDate}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // STATUS
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when (rental.status) {
                                    RentalStatus.ACTIVE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    RentalStatus.PENDING -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                                    RentalStatus.COMPLETED -> MaterialTheme.colorScheme.surfaceVariant
                                    RentalStatus.CANCELLED -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                                }
                            ) {
                                Text(
                                    text = rental.status.name,
                                    modifier = Modifier.padding(8.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Button(
                                onClick = {
                                    //API FUTURO: aceptar / activar
                                },
                                enabled = rental.status == RentalStatus.PENDING,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Aceptar")
                            }

                            OutlinedButton(
                                onClick = {
                                    //API FUTURO: cancelar
                                },
                                enabled = rental.status != RentalStatus.COMPLETED,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}