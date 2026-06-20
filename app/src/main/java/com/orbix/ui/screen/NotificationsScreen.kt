package com.orbix.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Notification(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val icon: ImageVector,
    val type: NotificationType
)

enum class NotificationType {
    ALERT, INFO, SUCCESS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val notifications = listOf(
        Notification(
            id = "1",
            title = "Reserva por vencer",
            description = "Tu reserva del Toyota Yaris finaliza mañana a las 10:00 AM. Recuerda devolver el vehículo con el tanque lleno.",
            date = "Hoy, 09:30 AM",
            icon = Icons.Default.CalendarToday,
            type = NotificationType.ALERT
        ),
        Notification(
            id = "2",
            title = "Verificación aprobada",
            description = "Felicidades, tus documentos han sido verificados con éxito. Ya puedes rentar cualquier vehículo en la plataforma.",
            date = "Ayer, 04:15 PM",
            icon = Icons.Default.CheckCircle,
            type = NotificationType.SUCCESS
        ),
        Notification(
            id = "3",
            title = "Recordatorio de entrega",
            description = "Debes coordinar la entrega del Suzuki Swift con el propietario a través del chat de la app.",
            date = "Hace 2 días",
            icon = Icons.Default.Info,
            type = NotificationType.INFO
        ),
        Notification(
            id = "4",
            title = "Mantenimiento programado",
            description = "Se recuerda a los arrendadores mantener sus registros de emisión al día para evitar penalizaciones.",
            date = "Hace 3 días",
            icon = Icons.Default.Warning,
            type = NotificationType.ALERT
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
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
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
    val iconColor = when (notification.type) {
        NotificationType.ALERT -> MaterialTheme.colorScheme.error
        NotificationType.INFO -> MaterialTheme.colorScheme.primary
        NotificationType.SUCCESS -> MaterialTheme.colorScheme.tertiary
    }

    val iconBgColor = when (notification.type) {
        NotificationType.ALERT -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        NotificationType.INFO -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        NotificationType.SUCCESS -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = iconBgColor,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = notification.icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = notification.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
