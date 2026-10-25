package com.example.cineflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cineflow.data.model.AspectRatioOption
import com.example.cineflow.data.model.AudioSoundscape
import com.example.cineflow.data.model.CameraMotion
import com.example.cineflow.data.model.DurationOption
import com.example.cineflow.data.model.GenreCategory
import com.example.cineflow.data.model.ResolutionOption
import com.example.cineflow.data.model.StylePreset
import com.example.cineflow.ui.AppScreen
import com.example.cineflow.ui.CineFlowViewModel
import com.example.ui.theme.CineAmber
import com.example.ui.theme.CineAmberBright
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineCardSurface
import com.example.ui.theme.CineCyan
import com.example.ui.theme.CineDarkSurface
import com.example.ui.theme.CineGold
import com.example.ui.theme.CineNeonPink
import com.example.ui.theme.CineObsidian
import com.example.ui.theme.CineSuccess
import com.example.ui.theme.CineSurfaceVariant
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: CineFlowViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.studioState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize().background(CineObsidian)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top App Bar / Brand Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(CineAmber, CineNeonPink)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = "CineFlow Logo",
                            tint = CineObsidian,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "CineFlow",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = CineTextPrimary
                        )
                        Text(
                            text = "AI Text-to-Video Studio",
                            style = MaterialTheme.typography.bodySmall,
                            color = CineAmberBright
                        )
                    }
                }

                // Unlimited Free Badge
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CineSuccess.copy(alpha = 0.15f))
                        .border(1.dp, CineSuccess.copy(alpha = 0.4f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AllInclusive,
                        contentDescription = "Unlimited Free",
                        tint = CineSuccess,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "100% Free • Unlimited",
                        style = MaterialTheme.typography.labelSmall,
                        color = CineSuccess,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

                // Quick Surprise Me / CineRoll Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.navigateTo(AppScreen.RANDOMIZER) }
                    .padding(bottom = 10.dp),
                colors = CardDefaults.cardColors(containerColor = CineCardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CineAmber.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CineAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = "Dice",
                                tint = CineAmber,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Need inspiration? Roll CineDice",
                                style = MaterialTheme.typography.titleSmall,
                                color = CineTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Instant random prompts in any genre or style",
                                style = MaterialTheme.typography.bodySmall,
                                color = CineTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Text(
                        text = "Roll ➔",
                        style = MaterialTheme.typography.labelMedium,
                        color = CineAmber,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // AUTO GENERATE & SOCIAL UPLOAD BANNER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.startAutoPilotGenerateAndPublish(context) }
                    .padding(bottom = 14.dp)
                    .testTag("auto_generate_social_button"),
                colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(CineCyan, CineNeonPink, CineAmber)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(androidx.compose.ui.graphics.Brush.linearGradient(listOf(CineCyan, CineNeonPink))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Auto Upload",
                                tint = CineObsidian,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "⚡ Auto-Generate & Upload",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = CineTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Auto-Pilot",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CineCyan,
                                    fontSize = 10.sp
                                )
                            }
                            Text(
                                text = "One-tap AI creation + direct upload to Shorts, TikTok, Reels & X",
                                style = MaterialTheme.typography.bodySmall,
                                color = CineTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Text(
                        text = "Auto ➔",
                        style = MaterialTheme.typography.labelMedium,
                        color = CineAmber,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // PROMPT INPUT SECTION
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header with Multilingual indicator
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Descriptive Prompt",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CineTextPrimary
                            )
                        }

                        // Multilingual Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CineCyan.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Languages",
                                tint = CineCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "All Languages Supported",
                                style = MaterialTheme.typography.labelSmall,
                                color = CineCyan,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Prompt TextField
                    OutlinedTextField(
                        value = state.promptText,
                        onValueChange = { viewModel.updatePromptText(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("prompt_input_field"),
                        placeholder = {
                            Text(
                                text = "Describe your cinematic scene in detail (e.g. A neon-lit cybernetic samurai walking through a rainy Tokyo alley at midnight, slow-motion water droplets, 35mm film...)",
                                color = CineTextMuted,
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CineAmber,
                            unfocusedBorderColor = CineBorder,
                            focusedTextColor = CineTextPrimary,
                            unfocusedTextColor = CineTextPrimary,
                            cursorColor = CineAmber,
                            focusedContainerColor = CineObsidian,
                            unfocusedContainerColor = CineObsidian
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action buttons below prompt (Paste, Clear, AI Enhance)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            // Paste button
                            IconButton(
                                onClick = {
                                    clipboardManager.getText()?.let {
                                        viewModel.updatePromptText(it.text)
                                    }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = CineTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Clear button
                            if (state.promptText.isNotBlank()) {
                                IconButton(
                                    onClick = { viewModel.updatePromptText("") },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = CineTextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // AI Enhance for Cinema Button
                        Button(
                            onClick = { viewModel.enhancePrompt() },
                            enabled = state.promptText.isNotBlank() && !state.isEnhancingPrompt,
                            modifier = Modifier.testTag("enhance_prompt_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CineSurfaceVariant,
                                contentColor = CineAmberBright
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CineAmber.copy(alpha = 0.5f))
                        ) {
                            if (state.isEnhancingPrompt) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    color = CineAmber,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Enhancing...", fontSize = 12.sp)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Enhance",
                                    tint = CineAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("AI Enhance for Cinema", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Display Enhanced Prompt if active
                    state.enhancedPrompt?.let { enhanced ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CineAmber.copy(alpha = 0.08f))
                                .border(1.dp, CineAmber.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Enhanced",
                                        tint = CineAmber,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Director's Script Enhanced (${state.detectedLanguage})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CineAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = enhanced,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CineTextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // DURATION SELECTOR (WITH 10-MINUTES EPIC CINEMA PROMINENTLY)
            Text(
                text = "Video Duration",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CineTextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DurationOption.entries.forEach { option ->
                    val isSelected = state.durationSeconds == option.seconds
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when {
                                    isSelected && option.isEpic -> CineAmber
                                    isSelected -> CineSurfaceVariant
                                    option.isEpic -> CineAmber.copy(alpha = 0.12f)
                                    else -> CineDarkSurface
                                }
                            )
                            .border(
                                1.dp,
                                when {
                                    isSelected -> CineAmber
                                    option.isEpic -> CineAmber.copy(alpha = 0.5f)
                                    else -> CineBorder
                                },
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.selectDuration(option.seconds) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("duration_${option.seconds}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (option.isEpic) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Epic",
                                        tint = if (isSelected) CineObsidian else CineAmber,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                }
                                Text(
                                    text = option.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected && option.isEpic) CineObsidian else if (isSelected) CineAmber else CineTextPrimary
                                )
                            }
                            Text(
                                text = option.description,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                color = if (isSelected && option.isEpic) CineObsidian else CineTextMuted
                            )
                        }
                    }
                }
            }

            // CINEMATIC STYLE SELECTOR
            Text(
                text = "Cinematic Visual Style",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CineTextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StylePreset.entries.forEach { style ->
                    val isSelected = state.selectedStyle == style
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) CineSurfaceVariant else CineDarkSurface)
                            .border(
                                1.5.dp,
                                if (isSelected) CineAmber else CineBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.selectStyle(style) }
                            .padding(10.dp)
                            .testTag("style_${style.name.lowercase()}"),
                    ) {
                        Column {
                            Text(
                                text = style.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CineAmber else CineTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = style.tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = CineCyan,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = style.colorTone,
                                style = MaterialTheme.typography.bodySmall,
                                color = CineTextMuted,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // CAMERA MOTION SELECTOR
            Text(
                text = "Camera Trajectory & Motion",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CineTextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CameraMotion.entries.forEach { motion ->
                    val isSelected = state.selectedCamera == motion
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CineAmber.copy(alpha = 0.18f) else CineDarkSurface)
                            .border(
                                1.dp,
                                if (isSelected) CineAmber else CineBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.selectCamera(motion) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("camera_${motion.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = motion.displayName,
                                tint = if (isSelected) CineAmber else CineTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = motion.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) CineAmber else CineTextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // ASPECT RATIO & RESOLUTION SECTION
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Aspect Ratio Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Aspect Ratio",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AspectRatioOption.entries.forEach { ratio ->
                            val isSelected = state.selectedAspectRatio == ratio
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) CineAmber else CineDarkSurface)
                                    .border(1.dp, if (isSelected) CineAmber else CineBorder, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.selectAspectRatio(ratio) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                    .testTag("ratio_${ratio.label.replace(":", "_")}")
                            ) {
                                Text(
                                    text = ratio.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) CineObsidian else CineTextPrimary
                                )
                            }
                        }
                    }
                }

                // Resolution Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Quality",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ResolutionOption.entries.forEach { res ->
                            val isSelected = state.selectedResolution == res
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) CineCyan else CineDarkSurface)
                                    .border(1.dp, if (isSelected) CineCyan else CineBorder, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.selectResolution(res) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                    .testTag("res_${res.fps}fps")
                            ) {
                                Text(
                                    text = res.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) CineObsidian else CineTextPrimary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            // SOUNDSCAPE PICKER
            Text(
                text = "Audio Soundscape Synthesis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CineTextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AudioSoundscape.entries.forEach { audio ->
                    val isSelected = state.selectedAudio == audio
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CineSurfaceVariant else CineDarkSurface)
                            .border(1.dp, if (isSelected) CineAmber else CineBorder, RoundedCornerShape(8.dp))
                            .clickable { viewModel.selectAudio(audio) }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = audio.displayName,
                                tint = if (isSelected) CineAmber else CineTextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = audio.displayName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) CineAmber else CineTextPrimary
                                )
                                Text(
                                    text = audio.genreTheme,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    color = CineTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // PRIMARY ACTION: RENDER CINEMATIC VIDEO BUTTON
            Button(
                onClick = { viewModel.startRenderVideo(context) },
                enabled = state.promptText.isNotBlank() && !state.isRendering,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("render_video_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CineAmber,
                    contentColor = CineObsidian
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Render",
                        tint = CineObsidian,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.durationSeconds >= 600) "RENDER 10-MIN EPIC CINEMA" else "RENDER CINEMATIC VIDEO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CineObsidian
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // RENDERING PROGRESS DIALOG
        if (state.isRendering) {
            Dialog(onDismissRequest = { /* Modal during render */ }) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CineAmber)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(CineAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { state.renderProgress },
                                modifier = Modifier.size(54.dp),
                                color = CineAmber,
                                strokeWidth = 4.dp
                            )
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = CineAmber,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Synthesizing Cinematic Video",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CineTextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = state.renderStatusMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = CineAmberBright,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { state.renderProgress },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = CineAmber,
                            trackColor = CineBorder
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${(state.renderProgress * 100).toInt()}% • High-Definition 60FPS",
                            style = MaterialTheme.typography.labelSmall,
                            color = CineTextMuted
                        )
                    }
                }
            }
        }
    }
}
