package com.orbix.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orbix.R
import com.orbix.ui.theme.OrangePrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLogin: () -> Unit) {

    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.height(200.dp))
        Image(
            modifier = Modifier.size(100.dp).clip(CircleShape),
            painter = painterResource(id = R.drawable.orbix512),
            contentDescription = null // Imagen decorativa
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Orbix", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(40.dp))
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        )
        {
            Column(modifier = Modifier.padding(24.dp))
            {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (showPassword)
                        androidx.compose.ui.text.input.VisualTransformation.None
                    else
                        androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword })
                        {
                            Icon(
                                imageVector = if (showPassword)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar contraseña"

                            )
                        }
                    }

                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            delay(750)
                            isLoading = false
                            onLogin()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                )
                {
                    if (isLoading) {
                        CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Iniciar sesión", color = White)
                    }
                }

            }
        }
    }
}