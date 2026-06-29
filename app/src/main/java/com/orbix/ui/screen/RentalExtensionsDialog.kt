package com.orbix.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orbix.ui.model.RentalResponse
import com.orbix.ui.model.RentalStatus
import com.orbix.ui.util.formatPrice
import com.orbix.ui.viewmodel.RentalViewModel

@Composable
fun RentalExtensionsDialog(
    rental: RentalResponse,
    isHost: Boolean,
    viewModel: RentalViewModel,
    onDismiss: () -> Unit
) {
    var showRequestDialog by remember { mutableStateOf(false) }

    LaunchedEffect(rental.id) {
        viewModel.loadExtensionsForRental(rental.id)
    }

    if (showRequestDialog) {
        val pricePerDay = rental.totalPrecio / rental.totalDias
        RequestExtensionDialog(
            pricePerDay = pricePerDay,
            isSubmitting = viewModel.isSubmitting,
            onDismiss = { showRequestDialog = false },
            onConfirm = { dias ->
                viewModel.createExtension(rental.id, dias) {
                    showRequestDialog = false
                    viewModel.loadExtensionsForRental(rental.id)
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Extensiones de Renta",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                if (viewModel.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (viewModel.extensionRequests.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay solicitudes de extensión.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(viewModel.extensionRequests, key = { it.id }) { extension ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "+${extension.diasExtension} día(s)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        RentalStatusChip(status = extension.estado)
                                    }

                                    Text(
                                        text = "Nueva fecha fin: ${extension.nuevaFechaFin}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = "Costo adicional: $${formatPrice(extension.costoAdicional)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    if (isHost && extension.estado == RentalStatus.PENDIENTE) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    viewModel.approveExtension(extension.id) {
                                                        viewModel.loadExtensionsForRental(rental.id)
                                                    }
                                                },
                                                enabled = !viewModel.isSubmitting,
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text("Aprobar", style = MaterialTheme.typography.bodySmall)
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    viewModel.rejectExtension(extension.id) {
                                                        viewModel.loadExtensionsForRental(rental.id)
                                                    }
                                                },
                                                enabled = !viewModel.isSubmitting,
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(
                                                    Icons.Default.Close,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text("Rechazar", style = MaterialTheme.typography.bodySmall)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Error or success messages
                viewModel.submitErrorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                viewModel.successMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            if (!isHost && rental.estado == RentalStatus.APROBADA) {
                val hasPending = viewModel.extensionRequests.any { it.estado == RentalStatus.PENDIENTE }
                Button(
                    onClick = {
                        viewModel.clearMessages()
                        showRequestDialog = true
                    },
                    enabled = !hasPending && !viewModel.isLoading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Solicitar Extensión")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
