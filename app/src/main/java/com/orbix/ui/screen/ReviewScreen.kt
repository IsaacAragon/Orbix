package com.orbix.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.orbix.ui.viewmodel.ReviewViewModel
import com.orbix.ui.viewmodel.VehicleViewModel

@Composable
fun CarReviewScreen(
    vehicleId: Long,
    onBack: () -> Unit,
    onReviewSubmitted: () -> Unit,
    viewModel: ReviewViewModel = viewModel()
) {
    val activity = androidx.compose.ui.platform.LocalContext.current as? androidx.activity.ComponentActivity
    val vehicleViewModel: VehicleViewModel? = activity?.let { viewModel(viewModelStoreOwner = it) }
    val vehicle = vehicleViewModel?.vehicles?.find { it.id == vehicleId }

    LaunchedEffect(Unit) {
        viewModel.setReviewType(ReviewViewModel.TYPE_VEHICLE)
        viewModel.loadAllTags()
    }

    ReviewFormScreen(
        title = "Califica el Vehículo",
        subtitle = if (vehicle != null) {
            "¡Cuéntanos cómo fue tu experiencia con el ${vehicle.brand} ${vehicle.model}!"
        } else {
            "¡Cuéntanos cómo fue tu experiencia con el auto!"
        },
        icon = Icons.Default.DirectionsCar,
        imageUrl = vehicle?.imageUrl,
        buttonText = "Enviar Reseña del Auto",
        viewModel = viewModel,
        onBack = onBack,
        onSubmit = { rating, comment ->
            viewModel.submitVehicleReview(vehicleId, rating, comment, onReviewSubmitted)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserReviewScreen(
    reviewedUserId: Long,
    onBack: () -> Unit,
    onReviewSubmitted: () -> Unit,
    viewModel: ReviewViewModel = viewModel()
) {
    LaunchedEffect(reviewedUserId) {
        viewModel.setReviewType(ReviewViewModel.TYPE_USER)
        viewModel.loadAllTags()
        viewModel.loadUserReviews(reviewedUserId)
    }

    val summary = viewModel.userSummary
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val availableTags by viewModel.availableTags.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Reseña",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
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
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = summary?.nombre ?: "Cliente",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                if (summary != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", summary.averageRating),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " · ${summary.sentimentLabel}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    summary.memberSinceYear?.let { year ->
                        Text(
                            text = "En Orbix desde $year",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ReviewFormCard(
                    rating = rating,
                    onRatingChange = { newRating ->
                        rating = newRating
                        viewModel.onRatingChanged(newRating)
                        viewModel.clearError()
                    },
                    availableTags = availableTags,
                    selectedTags = selectedTags,
                    isLoadingTags = viewModel.isLoadingTags,
                    onTagToggle = {
                        viewModel.toggleTag(it)
                        viewModel.clearError()
                    },
                    comment = comment,
                    onCommentChange = {
                        comment = it
                        viewModel.clearError()
                    },
                    errorMessage = viewModel.errorMessage
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.submitUserReview(
                            reviewedUserId,
                            rating,
                            comment,
                            onReviewSubmitted
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = rating > 0 && !viewModel.isSubmitting
                ) {
                    if (viewModel.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Enviar reseña", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewFormScreen(
    title: String,
    subtitle: String,
    icon: ImageVector,
    imageUrl: String? = null,
    buttonText: String,
    viewModel: ReviewViewModel,
    onBack: () -> Unit,
    onSubmit: (Int, String) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val availableTags by viewModel.availableTags.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Reseña",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
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
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                ReviewFormCard(
                    rating = rating,
                    onRatingChange = { newRating ->
                        rating = newRating
                        viewModel.onRatingChanged(newRating)
                        viewModel.clearError()
                    },
                    availableTags = availableTags,
                    selectedTags = selectedTags,
                    isLoadingTags = viewModel.isLoadingTags,
                    onTagToggle = {
                        viewModel.toggleTag(it)
                        viewModel.clearError()
                    },
                    comment = comment,
                    onCommentChange = {
                        comment = it
                        viewModel.clearError()
                    },
                    errorMessage = viewModel.errorMessage
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = { onSubmit(rating, comment) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = rating > 0 && !viewModel.isSubmitting
                ) {
                    if (viewModel.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(buttonText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewFormCard(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    availableTags: List<com.orbix.ui.model.ReviewTagOption>,
    selectedTags: Set<String>,
    isLoadingTags: Boolean,
    onTagToggle: (String) -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
    errorMessage: String?
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "¿Qué puntuación le darías?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(5) { index ->
                    val isSelected = index < rating
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.25f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "star_scale"
                    )
                    IconButton(
                        onClick = { onRatingChange(index + 1) },
                        modifier = Modifier
                            .size(48.dp)
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = rating > 0,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isLoadingTags) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                        ReviewTagsSection(
                            tags = availableTags,
                            selected = selectedTags,
                            onToggle = onTagToggle
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = comment,
                        onValueChange = onCommentChange,
                        label = { Text("Comentario (opcional)") },
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

            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
