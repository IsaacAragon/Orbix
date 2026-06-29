package com.orbix.ui.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orbix.ui.util.formatPrice
import com.orbix.ui.util.rentalDaysBetween
import com.orbix.ui.util.toApiDate
import com.orbix.ui.util.validateRentalDates
import java.time.LocalDate
import java.util.Calendar

@Composable
fun RentVehicleDialog(
    pricePerDay: Double,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (fechaInicio: String, fechaFin: String) -> Unit
) {
    val context = LocalContext.current
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var startDisplay by remember { mutableStateOf("") }
    var endDisplay by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf<String?>(null) }

    fun openDatePicker(isStart: Boolean) {
        val today = Calendar.getInstance()
        val initial = if (isStart) {
            startDate?.let {
                Calendar.getInstance().apply {
                    set(it.year, it.monthValue - 1, it.dayOfMonth)
                }
            } ?: today
        } else {
            endDate?.let {
                Calendar.getInstance().apply {
                    set(it.year, it.monthValue - 1, it.dayOfMonth)
                }
            } ?: startDate?.let {
                Calendar.getInstance().apply {
                    set(it.year, it.monthValue - 1, it.dayOfMonth)
                }
            } ?: today
        }

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selected = LocalDate.of(year, month + 1, day)
                val apiDate = toApiDate(day, month + 1, year)
                val display = String.format("%02d/%02d/%d", day, month + 1, year)
                if (isStart) {
                    startDate = selected
                    startDisplay = display
                    if (endDate != null && endDate!!.isBefore(selected)) {
                        endDate = null
                        endDisplay = ""
                    }
                } else {
                    endDate = selected
                    endDisplay = display
                }
                dateError = null
            },
            initial.get(Calendar.YEAR),
            initial.get(Calendar.MONTH),
            initial.get(Calendar.DAY_OF_MONTH)
        ).apply {
            if (isStart) {
                datePicker.minDate = today.timeInMillis
            } else {
                val minCal = startDate?.let {
                    Calendar.getInstance().apply {
                        set(it.year, it.monthValue - 1, it.dayOfMonth)
                    }
                } ?: today
                datePicker.minDate = minCal.timeInMillis
            }
        }.show()
    }

    val days = if (startDate != null && endDate != null) {
        rentalDaysBetween(startDate.toString(), endDate.toString())
    } else {
        0L
    }
    val total = days * pricePerDay

    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        shape = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Today,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "Solicitar renta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Selecciona las fechas de tu renta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = startDisplay,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha inicio") },
                    placeholder = { Text("Seleccionar") },
                    trailingIcon = {
                        Icon(Icons.Default.Event, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isSubmitting) { openDatePicker(isStart = true) }
                )
                OutlinedTextField(
                    value = endDisplay,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha fin") },
                    placeholder = { Text("Seleccionar") },
                    trailingIcon = {
                        Icon(Icons.Default.Event, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isSubmitting && startDate != null) {
                            openDatePicker(isStart = false)
                        }
                )
                if (days > 0) {
                    Text(
                        text = "$${formatPrice(pricePerDay)} × $days día(s) = $${formatPrice(total)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                dateError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val start = startDate
                    val end = endDate
                    if (start == null || end == null) {
                        dateError = "Selecciona ambas fechas"
                        return@Button
                    }
                    val validation = validateRentalDates(start, end)
                    if (validation != null) {
                        dateError = validation
                        return@Button
                    }
                    onConfirm(start.toString(), end.toString())
                },
                enabled = !isSubmitting,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Confirmar solicitud")
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
