package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SportsVerseColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0F388A),
    onPrimaryContainer = Color.White,
    secondary = SportsOrange,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF663C00),
    onSecondaryContainer = Color.White,
    tertiary = NeonGreen,
    onTertiary = Color.Black,
    background = DarkMidnightBg,
    onBackground = TextWhite,
    surface = DeepCardBg,
    onSurface = TextWhite,
    surfaceVariant = Color(0xFF1E2B4C),
    onSurfaceVariant = TextMuted,
    outline = GlassBorder
)

@Composable
fun SportsVerseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SportsVerseColorScheme,
        typography = Typography,
        content = content
    )
}
