package com.example.cineflow.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cineflow.data.model.GenreCategory
import com.example.cineflow.ui.CineFlowViewModel
import com.example.ui.theme.CineAmber
import com.example.ui.theme.CineAmberBright
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineCardSurface
import com.example.ui.theme.CineCyan
import com.example.ui.theme.CineDarkSurface
import com.example.ui.theme.CineNeonPink
import com.example.ui.theme.CineObsidian
import com.example.ui.theme.CineSurfaceVariant
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary

@Composable
fun RandomPromptScreen(
    viewModel: CineFlowViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.randomizerState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val rotation = remember { Animatable(0f) }

    LaunchedEffect(state.isRolling) {
        if (state.isRolling) {
            rotation.animateTo(
                targetValue = rotation.value + 360f,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(modifier = modifier.fillMaxSize().background(CineObsidian)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CineRoll Generator",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary
                    )
                    Text(
                        text = "Infinite cinematic prompt combinations",
                        style = MaterialTheme.typography.bodySmall,
                        color = CineTextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CineAmber.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = "Dice",
                        tint = CineAmber,
                        modifier = Modifier.size(24.dp).rotate(rotation.value)
                    )
                }
            }

            // Genre Constrain Filters
            Text(
                text = "Constrain by Genre (Optional)",
                style = MaterialTheme.typography.labelMedium,
                color = CineTextSecondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                GenreCategory.entries.forEach { genre ->
                    val isSelected = state.preferredGenre == genre
                    FilterChipPill(
                        label = genre.displayName,
                        isSelected = isSelected,
                        onClick = { viewModel.setRandomizerGenre(genre) }
                    )
                }
            }

            // GENERATED RESULT CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CineAmber)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Badge row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PillTag(state.currentPrompt.genre.displayName, CineAmber)
                        PillTag(state.currentPrompt.style.displayName, CineCyan)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title
                    Text(
                        text = state.currentPrompt.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Prompt content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CineObsidian)
                            .border(1.dp, CineBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = state.currentPrompt.promptText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = CineTextPrimary,
                            lineHeight = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Directorial Specs
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CineSurfaceVariant.copy(alpha = 0.5f))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Camera",
                                tint = CineAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Camera: ${state.currentPrompt.cameraMotion.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CineTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Lighting: ${state.currentPrompt.lighting}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CineTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Copy action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(state.currentPrompt.promptText))
                                Toast.makeText(context, "Random prompt copied!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = CineTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Prompt", color = CineTextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }

            // ACTION BUTTONS (ROLL AGAIN & USE IN STUDIO)
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Roll Again Button
                OutlinedButton(
                    onClick = { viewModel.rollRandomPrompt() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("roll_again_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CineAmber)
                ) {
                    if (state.isRolling) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = CineAmber, strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Roll", tint = CineAmber)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Roll Again", color = CineAmber, fontWeight = FontWeight.Bold)
                    }
                }

                // Use in Studio Button
                Button(
                    onClick = { viewModel.applyRandomPromptToStudio() },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(52.dp)
                        .testTag("use_random_prompt_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CineAmber, contentColor = CineObsidian),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Use", tint = CineObsidian)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Use in Studio", color = CineObsidian, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
