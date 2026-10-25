package com.example.cineflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.cineflow.data.model.VideoClip
import com.example.cineflow.ui.AppScreen
import com.example.cineflow.ui.CineFlowViewModel
import com.example.ui.theme.CineAmber
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineCyan
import com.example.ui.theme.CineDarkSurface
import com.example.ui.theme.CineError
import com.example.ui.theme.CineNeonPink
import com.example.ui.theme.CineObsidian
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectsScreen(
    viewModel: CineFlowViewModel,
    modifier: Modifier = Modifier
) {
    val clips by viewModel.savedClips.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize().background(CineObsidian)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Clips & Gallery",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary
                    )
                    Text(
                        text = "${clips.size} high-definition rendered clips",
                        style = MaterialTheme.typography.bodySmall,
                        color = CineTextSecondary
                    )
                }

                Button(
                    onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
                    colors = ButtonDefaults.buttonColors(containerColor = CineAmber, contentColor = CineObsidian),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = CineObsidian, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Video", fontWeight = FontWeight.Bold)
                }
            }

            if (clips.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(CineDarkSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = CineAmber,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No Videos Rendered Yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CineTextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Describe your scene in Studio and create your first cinematic clip!",
                            style = MaterialTheme.typography.bodySmall,
                            color = CineTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
                            colors = ButtonDefaults.buttonColors(containerColor = CineAmber, contentColor = CineObsidian),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Open Video Studio", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(clips, key = { it.id }) { clip ->
                        ProjectClipCard(
                            clip = clip,
                            onPlay = { viewModel.openClipInPlayer(clip) },
                            onToggleFavorite = { viewModel.toggleFavorite(clip.id) },
                            onShare = { viewModel.openSocialPublishDialog(clip) },
                            onDelete = { viewModel.deleteClip(clip.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun ProjectClipCard(
    clip: VideoClip,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPlay),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Thumbnail / Stage Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (clip.previewDrawableRes != null) {
                    Image(
                        painter = painterResource(id = clip.previewDrawableRes),
                        contentDescription = clip.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    CinematicPlaybackCanvas(clip = clip, progress = 0.5f, modifier = Modifier.fillMaxSize())
                }

                // Center Play Icon Button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(CineAmber.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = CineObsidian,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Duration badge
                val formattedDuration = if (clip.durationSeconds >= 60) {
                    "${clip.durationSeconds / 60}m"
                } else {
                    "${clip.durationSeconds}s"
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$formattedDuration • ${clip.aspectRatio.label}",
                        style = MaterialTheme.typography.labelSmall,
                        color = CineAmber,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = clip.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CineTextPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )

                val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(clip.timestampMs))
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = CineTextMuted,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = clip.prompt,
                style = MaterialTheme.typography.bodySmall,
                color = CineTextSecondary,
                maxLines = 2,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Badges & Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PillTag(clip.style.displayName, CineCyan)
                    PillTag(clip.cameraMotion.displayName, CineAmber)
                }

                Row {
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (clip.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (clip.isFavorite) CineNeonPink else CineTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = CineTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = CineError.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
