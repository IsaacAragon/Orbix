package com.orbix.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orbix.ui.util.formatPrice

@Composable
fun RequestExtensionDialog(
    pricePerDay: Double,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (diasExtension: Int) -> Unit
) {
    var diasExtension by remember { mutableStateOf(1) }

    val total = diasExtension * pricePerDay

    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        shape = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "Solicitar extensión de renta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Cuántos días adicionales deseas solicitar para tu renta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = { if (diasExtension > 1) diasExtension-- },
                        enabled = !isSubmitting && diasExtension > 1
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Menos días")
                    }

                    Text(
                        text = "$diasExtension",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(
                        onClick = { diasExtension++ },
                        enabled = !isSubmitting
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Más días")
                    }
                }

                Text(
                    text = "$${formatPrice(pricePerDay)} × $diasExtension día(s) = $${formatPrice(total)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(diasExtension) },
                enabled = !isSubmitting,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar solicitud")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text("Cancelar")
            }
        }
    )
}
