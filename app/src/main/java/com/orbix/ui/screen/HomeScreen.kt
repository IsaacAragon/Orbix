package com.orbix.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orbix.ui.theme.OrangePrimary

@Composable
fun HomeScreen(onLogout : () -> Unit,
               onNavigateToCarReview : () -> Unit,
               onNavigateToUserReview : () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp))
        {
            Text(
                text = "Bienvenido a Orbix",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {onNavigateToCarReview()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(contentColor = OrangePrimary)
            ) {
                Text(
                    text = "Calificar Servicio", color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {onNavigateToUserReview()},
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(contentColor = OrangePrimary)
            ) {
                Text(
                    text = "Reseñar Usuario", color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onLogout() }) {
                Text(text = "Cerrar Sesión")
            }
        }
    }
}