package com.orbix.ui.screen
import android.widget.Button
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orbix.ui.theme.OrangePrimary
import com.orbix.ui.theme.OrangeSecondary


@Composable
fun CarReviewScreen(onReviewSubmitted: () -> Unit){
    ReviewScreenContent(
        title = "Califica el Vehiculo",
        subtitle = "Cuentanos como fue tu experiencia!",
        icon = Icons.Default.DirectionsCar,
        buttonText = "Enviar Reseña",
        onReviewSubmitted = onReviewSubmitted


    )
}
@Composable
fun UserReviewScreen(onReviewSubmitted: () -> Unit) {
    ReviewScreenContent(
        title = "Califica al Usuario",
        subtitle = "Cuentanos como fue tu experiencia!",
        icon = Icons.Default.Person,
        buttonText = "Enviar reseña",
        onReviewSubmitted = onReviewSubmitted
    )
}

@Composable
private fun ReviewScreenContent (
    title: String,
    subtitle: String,
    icon: ImageVector,
    buttonText: String,
    onReviewSubmitted: () -> Unit
){
    var rating by remember {mutableStateOf(0)}
    var comment by remember {mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(OrangeSecondary),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(45.dp),
                tint = OrangePrimary
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            subtitle,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(contentColor = White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row{
                    repeat(5) { index ->
                        IconButton(onClick = {rating = index + 1}) {
                            Icon(
                                imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Estrella ${index + 1}",
                                tint = OrangePrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = {comment = it},
                    label = {Text("Escribe tu comentario...")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onReviewSubmitted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = OrangePrimary),
                    enabled = rating > 0
                ) {
                    Text(buttonText, color = White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}