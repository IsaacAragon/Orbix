package com.orbix.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orbix.ui.model.RentalStatus
import com.orbix.ui.model.label

@Composable
fun RentalStatusChip(status: RentalStatus, modifier: Modifier = Modifier) {
    val (container, label) = when (status) {
        RentalStatus.PENDIENTE -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        RentalStatus.APROBADA -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        RentalStatus.RECHAZADA -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }
    AssistChip(
        onClick = {},
        label = {
            Text(
                text = status.label(),
                fontWeight = FontWeight.Bold
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = container,
            labelColor = label
        )
    )
}
