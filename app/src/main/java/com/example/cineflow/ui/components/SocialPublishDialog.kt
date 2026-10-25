package com.example.cineflow.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cineflow.data.model.SocialPlatform
import com.example.cineflow.data.model.VideoClip
import com.example.cineflow.ui.SocialPublishState
import com.example.ui.theme.CineAmber
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineDarkSurface
import com.example.ui.theme.CineObsidian
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary

@Composable
fun SocialPublishDialog(
    state: SocialPublishState,
    onSelectPlatform: (SocialPlatform) -> Unit,
    onUpdateCaption: (String) -> Unit,
    onUpload: (SocialPlatform, String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!state.isOpen || state.targetClip == null) return

    val clip = state.targetClip
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(state.selectedPlatform.colorHex))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Auto-Upload to Social Media",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CineTextPrimary
                        )
                        Text(
                            text = "Publish high-definition clip directly",
                            style = MaterialTheme.typography.bodySmall,
                            color = CineTextSecondary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = CineTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Social Platform Selector Chips
                Text(
                    text = "SELECT SOCIAL PLATFORM",
                    style = MaterialTheme.typography.labelSmall,
                    color = CineAmber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SocialPlatform.entries.forEach { platform ->
                        val isSelected = state.selectedPlatform == platform
                        val platformColor = Color(platform.colorHex)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) platformColor else CineObsidian)
                                .border(1.dp, if (isSelected) platformColor else CineBorder, RoundedCornerShape(8.dp))
                                .clickable { onSelectPlatform(platform) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = platform.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) CineObsidian else CineTextPrimary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Caption & Viral Hashtags Editor
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CAPTION & VIRAL HASHTAGS",
                        style = MaterialTheme.typography.labelSmall,
                        color = CineTextSecondary,
                        fontSize = 10.sp
                    )

                    Text(
                        text = "Copy Caption",
                        style = MaterialTheme.typography.labelSmall,
                        color = CineAmber,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            clipboardManager.setText(AnnotatedString(state.customCaption))
                            Toast.makeText(context, "Caption copied!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = state.customCaption,
                    onValueChange = onUpdateCaption,
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(state.selectedPlatform.colorHex),
                        unfocusedBorderColor = CineBorder,
                        focusedTextColor = CineTextPrimary,
                        unfocusedTextColor = CineTextPrimary,
                        cursorColor = CineAmber,
                        focusedContainerColor = CineObsidian,
                        unfocusedContainerColor = CineObsidian
                    ),
                    shape = RoundedCornerShape(10.dp),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Clip summary info
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CineObsidian)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = clip.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = CineTextPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = "Format: ${clip.aspectRatio.label} • ${clip.resolution.badge} • ${clip.durationSeconds}s",
                                style = MaterialTheme.typography.labelSmall,
                                color = CineTextMuted,
                                fontSize = 10.sp
                            )
                        }

                        Text(
                            text = "MP4 Attached",
                            style = MaterialTheme.typography.labelSmall,
                            color = CineAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary Upload Button
                val buttonColor = Color(state.selectedPlatform.colorHex)
                Button(
                    onClick = { onUpload(state.selectedPlatform, state.customCaption) },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("confirm_social_upload_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = CineObsidian),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Upload",
                        tint = CineObsidian,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Upload to ${state.selectedPlatform.displayName}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = CineObsidian
                    )
                }
            }
        }
    }
}
