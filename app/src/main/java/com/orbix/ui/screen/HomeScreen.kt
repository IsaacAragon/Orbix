package com.orbix.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orbix.ui.theme.OrangePrimary

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToCarReview: () -> Unit,
    onNavigateToUserReview: () -> Unit,
    onNavigateToReservations: () -> Unit
) {

    Scaffold(
        bottomBar = {
            BottomBar(
                onLogout = onLogout,
                onNavigateToReservations = onNavigateToReservations
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { Header() }

            item { SearchBar() }

            item { Categories() }

            item {
                Text(
                    "Disponibles cerca de ti",
                    fontWeight = FontWeight.Bold
                )
            }

            items(5) {
                CarCard(onNavigateToCarReview)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Orbix", fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Red)
                Text("Estelí, Nicaragua")
            }
        }

        Icon(Icons.Default.AccountCircle, contentDescription = null)
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Encuentra tu vehículo ideal...",
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Default.Tune, contentDescription = null)
    }
}

@Composable
fun Categories() {

    val categories = listOf("Coches", "SUVs", "Motos", "Eléctrico", "Pickup")
    var selectedIndex by remember { mutableStateOf(0) }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Categorías", fontWeight = FontWeight.Bold)
            Text("Ver todas", color = OrangePrimary)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            itemsIndexed(categories) { index, category ->

                val isSelected = index == selectedIndex

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Color(0xFF2962FF) else Color(0xFFF2F2F2)
                        )
                        .clickable { selectedIndex = index }
                        .padding(vertical = 16.dp)
                ) {
                    Icon(
                        Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        category,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun CarCard(onNavigateToCarReview: () -> Unit) {

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
                    Text("Toyota Yaris", fontWeight = FontWeight.Bold)
                    Text("Automático • 4 Asientos")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFA000))
                    Text("4.8")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEAEAEA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.DirectionsCar, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$25/día",
                    fontWeight = FontWeight.Bold,
                    color = OrangePrimary
                )

                Button(
                    onClick = { onNavigateToCarReview() },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text("Rentar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    onLogout: () -> Unit,
    onNavigateToReservations: () -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { onNavigateToReservations() },
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("Reservas") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text("Favoritos") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { onLogout() },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Perfil") }
        )
    }
}