package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CineDarkColorScheme = darkColorScheme(
    primary = CineAmber,
    onPrimary = CineObsidian,
    primaryContainer = CineCardSurface,
    onPrimaryContainer = CineAmberBright,
    secondary = CineCyan,
    onSecondary = CineObsidian,
    secondaryContainer = CineSurfaceVariant,
    onSecondaryContainer = CineCyan,
    tertiary = CineNeonPink,
    onTertiary = CineObsidian,
    background = CineObsidian,
    onBackground = CineTextPrimary,
    surface = CineDarkSurface,
    onSurface = CineTextPrimary,
    surfaceVariant = CineSurfaceVariant,
    onSurfaceVariant = CineTextSecondary,
    outline = CineBorder,
    outlineVariant = CineBorderGlow,
    error = CineError,
    onError = CineTextPrimary
)

@Composable
fun CineFlowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CineDarkColorScheme,
        typography = CineTypography,
        content = content
    )
}
