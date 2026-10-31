package com.borgesnath1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RendezVousColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = TextDarkOnGold,
    secondary = GoldLight,
    background = BlackBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextGray,
    outline = BorderSubtle
)

@Composable
fun RendezvousTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RendezVousColorScheme,
        typography = Typography,
        content = content
    )
}