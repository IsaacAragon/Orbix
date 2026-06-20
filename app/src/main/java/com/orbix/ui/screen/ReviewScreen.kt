package com.orbix.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class ReviewTagCategory { POSITIVE, NEUTRAL, NEGATIVE }

data class ReviewTag(val label: String, val category: ReviewTagCategory)

private val vehicleTags = listOf(
    ReviewTag("Vehículo limpio", ReviewTagCategory.POSITIVE),
    ReviewTag("Entrega puntual", ReviewTagCategory.POSITIVE),
    ReviewTag("Entrega rápida y fácil", ReviewTagCategory.POSITIVE),
    ReviewTag("Tal como en las fotos", ReviewTagCategory.POSITIVE),
    ReviewTag("Excelente estado", ReviewTagCategory.POSITIVE),
    ReviewTag("Muy cómodo", ReviewTagCategory.POSITIVE),
    ReviewTag("No era lo que esperaba", ReviewTagCategory.NEUTRAL),
    ReviewTag("Podría estar más limpio", ReviewTagCategory.NEUTRAL),
    ReviewTag("Entrega con retraso leve", ReviewTagCategory.NEUTRAL),
    ReviewTag("Descripción inexacta", ReviewTagCategory.NEUTRAL),
    ReviewTag("Diferente a las fotos", ReviewTagCategory.NEGATIVE),
    ReviewTag("Problemas de seguridad", ReviewTagCategory.NEGATIVE),
    ReviewTag("Vehículo sucio", ReviewTagCategory.NEGATIVE),
    ReviewTag("Entrega tardía", ReviewTagCategory.NEGATIVE),
    ReviewTag("Motor con fallas", ReviewTagCategory.NEGATIVE),
    ReviewTag("Olor desagradable", ReviewTagCategory.NEGATIVE),
)

private val hostTags = listOf(
    ReviewTag("Anfitrión amable", ReviewTagCategory.POSITIVE),
    ReviewTag("Respuesta rápida", ReviewTagCategory.POSITIVE),
    ReviewTag("Muy puntual", ReviewTagCategory.POSITIVE),
    ReviewTag("Instrucciones claras", ReviewTagCategory.POSITIVE),
    ReviewTag("Recomendado", ReviewTagCategory.POSITIVE),
    ReviewTag("No recibí asistencia", ReviewTagCategory.NEUTRAL),
    ReviewTag("Comunicación lenta", ReviewTagCategory.NEUTRAL),
    ReviewTag("Podría mejorar", ReviewTagCategory.NEUTRAL),
    ReviewTag("Expectativas no cumplidas", ReviewTagCategory.NEUTRAL),
    ReviewTag("Difícil de contactar", ReviewTagCategory.NEGATIVE),
    ReviewTag("Llegó tarde", ReviewTagCategory.NEGATIVE),
    ReviewTag("Instrucciones confusas", ReviewTagCategory.NEGATIVE),
    ReviewTag("Actitud grosera", ReviewTagCategory.NEGATIVE),
    ReviewTag("No recomendado", ReviewTagCategory.NEGATIVE),
)

private fun ratingBand(rating: Int): ReviewTagCategory? = when {
    rating >= 4    -> ReviewTagCategory.POSITIVE
    rating == 3    -> ReviewTagCategory.NEUTRAL
    rating in 1..2 -> ReviewTagCategory.NEGATIVE
    else           -> null
}

@Composable
fun CarReviewScreen(onBack: () -> Unit, onReviewSubmitted: () -> Unit) {
    ReviewScreenContent(
        title = "Califica el Vehículo",
        subtitle = "¡Cuéntanos cómo fue tu experiencia con el auto!",
        icon = Icons.Default.DirectionsCar,
        buttonText = "Enviar Reseña del Auto",
        tags = vehicleTags,
        onBack = onBack,
        onReviewSubmitted = onReviewSubmitted
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserReviewScreen(onBack: () -> Unit, onReviewSubmitted: () -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    val scrollState = rememberScrollState()

    val currentBand = ratingBand(rating)
    LaunchedEffect(currentBand) {
        selectedTags = emptySet()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reseña", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(RoundedCornerShape(48.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Luis Aragón",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "4.9",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "En Orbix desde 2026",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Reseñas Muy positivas",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(5) { index ->
                                IconButton(
                                    onClick = { rating = index + 1 },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                                        contentDescription = "Estrella ${index + 1}",
                                        tint = if (index < rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = rating > 0,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(20.dp))
                                RatingTagsSection(
                                    rating = rating,
                                    tags = hostTags,
                                    selectedTags = selectedTags,
                                    onTagToggled = { tag ->
                                        selectedTags = if (tag in selectedTags) selectedTags - tag else selectedTags + tag
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            placeholder = { Text("Comentario... (Opcional)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = onReviewSubmitted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = rating > 0
                ) {
                    Text(
                        text = "Enviar reseña",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewScreenContent(
    title: String,
    subtitle: String,
    icon: ImageVector,
    buttonText: String,
    tags: List<ReviewTag>,
    onBack: () -> Unit,
    onReviewSubmitted: () -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    val currentBand = ratingBand(rating)
    LaunchedEffect(currentBand) {
        selectedTags = emptySet()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reseña", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Qué puntuación le darías?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { index ->
                            IconButton(
                                onClick = { rating = index + 1 },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Estrella ${index + 1}",
                                    tint = if (index < rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = rating > 0,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(20.dp))
                            RatingTagsSection(
                                rating = rating,
                                tags = tags,
                                selectedTags = selectedTags,
                                onTagToggled = { tag ->
                                    selectedTags = if (tag in selectedTags) selectedTags - tag else selectedTags + tag
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Escribe tu comentario...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onReviewSubmitted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = rating > 0
                    ) {
                        Text(
                            text = buttonText,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RatingTagsSection(
    rating: Int,
    tags: List<ReviewTag>,
    selectedTags: Set<String>,
    onTagToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTags = when {
        rating >= 4 -> tags.filter { it.category == ReviewTagCategory.POSITIVE }
        rating == 3 -> tags.filter { it.category == ReviewTagCategory.NEUTRAL }
        else        -> tags.filter { it.category == ReviewTagCategory.NEGATIVE }
    }

    val labelText = when {
        rating >= 4 -> "¿Qué destacó?"
        rating == 3 -> "¿Qué podría mejorar?"
        else        -> "¿Qué salió mal?"
    }

    val labelColor = when {
        rating >= 4 -> MaterialTheme.colorScheme.primary
        rating == 3 -> MaterialTheme.colorScheme.tertiary
        else        -> MaterialTheme.colorScheme.error
    }

    val selectedContainer = when {
        rating >= 4 -> MaterialTheme.colorScheme.primaryContainer
        rating == 3 -> MaterialTheme.colorScheme.tertiaryContainer
        else        -> MaterialTheme.colorScheme.errorContainer
    }

    val selectedLabelColor = when {
        rating >= 4 -> MaterialTheme.colorScheme.onPrimaryContainer
        rating == 3 -> MaterialTheme.colorScheme.onTertiaryContainer
        else        -> MaterialTheme.colorScheme.onErrorContainer
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = labelText,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = labelColor
        )

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            activeTags.forEach { tag ->
                val isSelected = tag.label in selectedTags
                FilterChip(
                    selected = isSelected,
                    onClick = { onTagToggled(tag.label) },
                    label = {
                        Text(
                            text = tag.label,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = selectedContainer,
                        selectedLabelColor = selectedLabelColor
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        selectedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}