package com.example.cineflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflow.ui.AppScreen
import com.example.ui.theme.CineAmber
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineDarkSurface
import com.example.ui.theme.CineObsidian
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary

sealed class BottomNavItem(
    val screen: AppScreen,
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    data object Studio : BottomNavItem(AppScreen.STUDIO, "Studio", Icons.Filled.Movie, Icons.Outlined.Movie)
    data object Library : BottomNavItem(AppScreen.LIBRARY, "Library", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary)
    data object CineRoll : BottomNavItem(AppScreen.RANDOMIZER, "CineRoll", Icons.Filled.Casino, Icons.Outlined.Casino)
    data object Player : BottomNavItem(AppScreen.PLAYER, "Cinema", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircle)
    data object Projects : BottomNavItem(AppScreen.PROJECTS, "My Clips", Icons.Filled.AutoAwesome, Icons.Filled.AutoAwesome)
}

@Composable
fun CineFlowBottomBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Studio,
        BottomNavItem.Library,
        BottomNavItem.CineRoll,
        BottomNavItem.Player,
        BottomNavItem.Projects
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CineDarkSurface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Top divider line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CineBorder)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen

                Column(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onNavigate(item.screen) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("nav_${item.title.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp, 28.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) CineAmber.copy(alpha = 0.16f) else androidx.compose.ui.graphics.Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                            contentDescription = item.title,
                            tint = if (isSelected) CineAmber else CineTextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = if (isSelected) CineTextPrimary else CineTextMuted,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
