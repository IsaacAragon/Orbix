package com.orbix.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orbix.ui.theme.OrangePrimary

@Composable
fun ReservationsScreen(
    onBack: () -> Unit,
    onRateService: () -> Unit
) {

    Scaffold(
        topBar = { TopBar(onBack) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // RESERVA ACTUAL
            item {
                Text(
                    "Tu reserva actual",
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                ActiveReservationCard()
            }

            // HISTORIAL
            item {
                Text(
                    "Historial",
                    fontWeight = FontWeight.Bold
                )
            }

            items(5) {
                HistoryCard(onRateService)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Reservas") },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
fun ActiveReservationCard() {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)) // naranja suave
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Badge "En curso"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(OrangePrimary.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    "En curso",
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Toyota Corolla",
                fontWeight = FontWeight.Black
            )

            Text(
                "Automático • 5 Asientos",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFD5D5D5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "$30/día",
                color = OrangePrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HistoryCard(onRateService: () -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Hyundai Tucson", fontWeight = FontWeight.Bold)
                    Text("Automático • 5 Asientos")
                }

                Text(
                    "Finalizado",
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEAEAEA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "$28/día",
                    fontWeight = FontWeight.Bold,
                    color = OrangePrimary
                )

                Button(
                    onClick = { onRateService() },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text("Calificar servicio", color = Color.White)
                }
            }
        }
    }
}