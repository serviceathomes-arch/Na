package com.example.cineflow.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.cineflow.data.model.AspectRatioOption
import com.example.cineflow.data.model.CameraMotion
import com.example.cineflow.data.model.StylePreset
import com.example.cineflow.data.model.VideoClip
import com.example.cineflow.ui.AppScreen
import com.example.cineflow.ui.CineFlowViewModel
import com.example.ui.theme.CineAmber
import com.example.ui.theme.CineAmberBright
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineCardSurface
import com.example.ui.theme.CineCyan
import com.example.ui.theme.CineDarkSurface
import com.example.ui.theme.CineError
import com.example.ui.theme.CineNeonPink
import com.example.ui.theme.CineObsidian
import com.example.ui.theme.CineSurfaceVariant
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlayerScreen(
    viewModel: CineFlowViewModel,
    modifier: Modifier = Modifier
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val clip = playerState.activeClip
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var playheadProgress by remember { mutableFloatStateOf(0f) }

    // 60FPS continuous playback loop
    LaunchedEffect(playerState.isPlaying, playerState.playbackSpeed, clip?.durationSeconds) {
        val duration = (clip?.durationSeconds ?: 6) * 1000L
        var lastTime = 0L

        while (true) {
            withFrameNanos { frameNanos ->
                if (lastTime == 0L) {
                    lastTime = frameNanos
                } else if (playerState.isPlaying && duration > 0) {
                    val deltaMs = (frameNanos - lastTime) / 1_000_000f
                    val step = (deltaMs / duration) * playerState.playbackSpeed
                    var next = playheadProgress + step
                    if (next >= 1.0f) {
                        next = if (playerState.isLooping) 0f else 1.0f
                    }
                    playheadProgress = next
                    viewModel.setPlayerProgress(next)
                    lastTime = frameNanos
                } else {
                    lastTime = frameNanos
                }
            }
        }
    }

    if (clip == null) {
        Box(
            modifier = modifier.fillMaxSize().background(CineObsidian),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = CineTextMuted,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No clip loaded",
                    style = MaterialTheme.typography.titleMedium,
                    color = CineTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
                    colors = ButtonDefaults.buttonColors(containerColor = CineAmber, contentColor = CineObsidian)
                ) {
                    Text("Go to Video Studio", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Box(modifier = modifier.fillMaxSize().background(CineObsidian)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 20.dp)
        ) {
            // Top Bar with Title, Favorite, Share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = clip.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "${clip.style.displayName} • ${clip.resolution.badge}",
                        style = MaterialTheme.typography.labelSmall,
                        color = CineAmberBright
                    )
                }

                Row {
                    // Favorite button
                    IconButton(onClick = { viewModel.toggleFavorite(clip.id) }) {
                        Icon(
                            imageVector = if (clip.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (clip.isFavorite) CineNeonPink else CineTextMuted
                        )
                    }

                    // Share to Social Media button
                    IconButton(
                        onClick = { viewModel.openSocialPublishDialog(clip) },
                        modifier = Modifier.testTag("player_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = CineAmber
                        )
                    }

                    // Delete button
                    IconButton(onClick = { viewModel.deleteClip(clip.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = CineError.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // CINEMATIC VIDEO CANVAS STAGE
            val targetRatio = playerState.activeAspectRatio ?: clip.aspectRatio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(targetRatio.ratio)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, CineBorder, RoundedCornerShape(8.dp))
                        .clickable { viewModel.togglePlayPause() }
                ) {
                    CinematicPlaybackCanvas(
                        clip = clip,
                        progress = playheadProgress,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay HUD indicator on pause
                    if (!playerState.isPlaying) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(CineAmber.copy(alpha = 0.9f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = CineObsidian,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    // Bottom info pill (Resolution + Duration)
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black.copy(alpha = 0.75f))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${clip.resolution.badge} • ${clip.durationSeconds}s",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = CineAmber
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // TIMELINE SCRUBBER
            val currentSeconds = (playheadProgress * clip.durationSeconds).toInt()
            val totalSeconds = clip.durationSeconds
            val formattedCurrent = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60)
            val formattedTotal = String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60)

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Slider(
                    value = playheadProgress,
                    onValueChange = {
                        playheadProgress = it
                        viewModel.setPlayerProgress(it)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("player_scrubber"),
                    colors = SliderDefaults.colors(
                        thumbColor = CineAmber,
                        activeTrackColor = CineAmber,
                        inactiveTrackColor = CineBorder
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formattedCurrent,
                        style = MaterialTheme.typography.labelSmall,
                        color = CineAmberBright
                    )
                    Text(
                        text = formattedTotal,
                        style = MaterialTheme.typography.labelSmall,
                        color = CineTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // PLAYBACK TRANSPORT CONTROLS
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind 10s
                IconButton(onClick = {
                    val step = 10f / clip.durationSeconds
                    playheadProgress = (playheadProgress - step).coerceAtLeast(0f)
                }) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Rewind",
                        tint = CineTextSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Play / Pause Primary
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CineAmber)
                        .clickable { viewModel.togglePlayPause() }
                        .testTag("player_play_pause_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                        tint = CineObsidian,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Forward 10s
                IconButton(onClick = {
                    val step = 10f / clip.durationSeconds
                    playheadProgress = (playheadProgress + step).coerceAtMost(1f)
                }) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Forward",
                        tint = CineTextSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SECONDARY CONTROLS (SPEED, LOOP, MUTE, ASPECT RATIO)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speed chips (0.5x, 1x, 2x)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(0.5f, 1.0f, 2.0f).forEach { speed ->
                        val isSelected = playerState.playbackSpeed == speed
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) CineAmber else CineDarkSurface)
                                .border(1.dp, if (isSelected) CineAmber else CineBorder, RoundedCornerShape(6.dp))
                                .clickable { viewModel.setPlaybackSpeed(speed) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${speed}x",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CineObsidian else CineTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Aspect Ratio switcher
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(AspectRatioOption.WIDESCREEN_16_9, AspectRatioOption.VERTICAL_9_16, AspectRatioOption.ANAMORPHIC_2_39).forEach { ratio ->
                        val isSelected = targetRatio == ratio
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) CineCyan else CineDarkSurface)
                                .border(1.dp, if (isSelected) CineCyan else CineBorder, RoundedCornerShape(6.dp))
                                .clickable { viewModel.setPlayerAspectRatio(ratio) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = ratio.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CineObsidian else CineTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Loop & Mute
                Row {
                    IconButton(onClick = { viewModel.toggleLoop() }, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Loop",
                            tint = if (playerState.isLooping) CineAmber else CineTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.toggleMute() }, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (playerState.isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Mute",
                            tint = if (playerState.isMuted) CineError else CineAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DIRECTOR'S CUT / METADATA ACCORDION
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleDirectorNotes() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CineAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Director's Cinematography Breakdown",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = CineTextPrimary
                            )
                        }

                        Text(
                            text = if (playerState.isShowingDirectorNotes) "Hide ▲" else "View ▼",
                            style = MaterialTheme.typography.labelSmall,
                            color = CineAmber
                        )
                    }

                    AnimatedVisibility(visible = playerState.isShowingDirectorNotes) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            DirectorSpecRow("Original Prompt", clip.prompt)
                            DirectorSpecRow("Director's Script", clip.enhancedPrompt)
                            DirectorSpecRow("Camera & Rig", "${clip.cameraMotion.displayName} • ${clip.directorBreakdown.cameraRig}")
                            DirectorSpecRow("Lens & Optics", clip.directorBreakdown.lensOptics)
                            DirectorSpecRow("Lighting Setup", clip.directorBreakdown.lightingSetup)
                            DirectorSpecRow("Color LUT", clip.directorBreakdown.colorGrading)
                            DirectorSpecRow("Particle Dynamics", clip.directorBreakdown.particleAtmosphere)
                            DirectorSpecRow("Sound Design", "${clip.audioSoundscape.displayName} • ${clip.directorBreakdown.audioCue}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DirectorSpecRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = CineAmber,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = CineTextPrimary,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
fun CinematicPlaybackCanvas(
    clip: VideoClip,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // 1. Background gradient representing scene lighting & atmosphere
        val topColor: Color
        val bottomColor: Color
        when (clip.style) {
            StylePreset.CYBER_NOIR -> {
                topColor = Color(0xFF070B18)
                bottomColor = Color(0xFF160620)
            }
            StylePreset.DARK_FANTASY -> {
                topColor = Color(0xFF180A0F)
                bottomColor = Color(0xFF080608)
            }
            StylePreset.ANIME_SHINKAI -> {
                topColor = Color(0xFF144686)
                bottomColor = Color(0xFFE2744B)
            }
            StylePreset.VINTAGE_35MM -> {
                topColor = Color(0xFF3E2D20)
                bottomColor = Color(0xFF1A140E)
            }
            else -> {
                topColor = Color(0xFF0A1224)
                bottomColor = Color(0xFF04060A)
            }
        }

        drawRect(
            brush = Brush.verticalGradient(listOf(topColor, bottomColor)),
            size = size
        )

        // 2. Parallax camera motion layers
        val panShiftX = when (clip.cameraMotion) {
            CameraMotion.PAN_HORIZONTAL -> (progress - 0.5f) * width * 0.16f
            CameraMotion.ORBIT_360 -> cos(progress * 6.28f) * width * 0.08f
            CameraMotion.DYNAMIC_HANDHELD -> sin(progress * 30f) * 6f
            else -> 0f
        }

        val panShiftY = when (clip.cameraMotion) {
            CameraMotion.DRONE_CRANE -> (progress - 0.5f) * height * 0.14f
            CameraMotion.TILT_UP -> -(progress - 0.5f) * height * 0.15f
            CameraMotion.DYNAMIC_HANDHELD -> cos(progress * 24f) * 5f
            else -> 0f
        }

        val zoomScale = when (clip.cameraMotion) {
            CameraMotion.DOLLY_PUSH -> 1.0f + (progress * 0.20f)
            CameraMotion.SLOW_MO_120FPS -> 1.05f + (sin(progress * 3.14f) * 0.03f)
            else -> 1.10f
        }

        // Midground geometric cinematic structures / horizon silhouette
        val horizonY = height * 0.65f + panShiftY
        val path = Path().apply {
            moveTo(-width * 0.2f, height)
            lineTo(-width * 0.2f, horizonY)
            lineTo(width * 0.2f + panShiftX * 0.5f, horizonY - 40f)
            lineTo(width * 0.5f + panShiftX * 0.5f, horizonY - 10f)
            lineTo(width * 0.8f + panShiftX * 0.5f, horizonY - 60f)
            lineTo(width * 1.2f, horizonY)
            lineTo(width * 1.2f, height)
            close()
        }
        drawPath(
            path = path,
            color = Color(0xFF050810).copy(alpha = 0.85f)
        )

        // Foreground silhouette element moving with stronger parallax
        val fgX = width * 0.5f + panShiftX
        val fgY = height * 0.75f + panShiftY
        val fgRadius = 40f * zoomScale
        drawCircle(
            color = Color(0xFF020406),
            radius = fgRadius,
            center = Offset(fgX, fgY)
        )

        // 3. Volumetric particles & lighting dynamics
        drawCinematicParticles(clip.style, progress, width, height)

        // 4. Anamorphic horizontal lens flare streak
        if (clip.style == StylePreset.CINEMATIC_8K || clip.style == StylePreset.CYBER_NOIR) {
            val flareY = height * 0.45f + (sin(progress * 4f) * 12f)
            val flareColor = if (clip.style == StylePreset.CYBER_NOIR) Color(0xFF00E5FF).copy(alpha = 0.4f) else Color(0xFFFFB703).copy(alpha = 0.45f)
            drawLine(
                color = flareColor,
                start = Offset(0f, flareY),
                end = Offset(width, flareY),
                strokeWidth = 2.5f
            )
        }

        // 5. Cinematic Vignette
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                center = Offset(width / 2f, height / 2f),
                radius = width.coerceAtLeast(height) * 0.7f
            )
        )
    }
}

fun DrawScope.drawCinematicParticles(
    style: StylePreset,
    progress: Float,
    width: Float,
    height: Float
) {
    val count = 32
    for (i in 0 until count) {
        val seed = i * 137.5f
        val speed = 0.25f + ((i % 5) * 0.15f)
        val p = (progress * speed + (seed / 1000f)) % 1f

        when (style) {
            StylePreset.CYBER_NOIR -> {
                // Rain streaks
                val x = ((seed * 19) % width)
                val y = (p * (height + 150)) - 75
                val color = if (i % 2 == 0) Color(0xFF00E5FF).copy(alpha = 0.6f) else Color(0xFFFF2E93).copy(alpha = 0.5f)
                drawLine(
                    color = color,
                    start = Offset(x, y),
                    end = Offset(x - 3f, y + 14f),
                    strokeWidth = 1.8f
                )
            }
            StylePreset.DARK_FANTASY -> {
                // Ascending embers
                val x = ((seed * 23) % width) + (sin(p * 8f + i) * 15f)
                val y = height - (p * (height + 50))
                val color = if (i % 3 == 0) Color(0xFFFF4D00).copy(alpha = 0.7f) else Color(0xFFFFB703).copy(alpha = 0.6f)
                drawCircle(color = color, radius = 2.5f + (i % 3), center = Offset(x, y))
            }
            StylePreset.ANIME_SHINKAI -> {
                // Shimmering celestial sparks
                val x = ((seed * 31) % width) + (cos(p * 5f + i) * 18f)
                val y = ((seed * 17) % height) - (p * 50f)
                drawCircle(color = Color(0xFFFFFFE0).copy(alpha = 0.65f), radius = 2.0f, center = Offset(x, y))
            }
            else -> {
                // Golden dust motes
                val x = ((seed * 29) % width) + (sin(p * 6f) * 10f)
                val y = ((seed * 13) % height) + (cos(p * 4f) * 8f)
                drawCircle(color = Color(0xFFFFD166).copy(alpha = 0.5f), radius = 2.2f, center = Offset(x, y))
            }
        }
    }
}
