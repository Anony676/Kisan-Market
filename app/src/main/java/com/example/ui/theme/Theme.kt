package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AgroGreenDarkPrimary,
    onPrimary = Color(0xFF052E16),
    primaryContainer = Color(0xFF14532D),
    onPrimaryContainer = Color(0xFFDCFCE7),
    secondary = HarvestGold,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFEF3C7),
    tertiary = EarthTerracotta,
    background = AgroGreenDarkBackground,
    onBackground = AgroTextDarkPrimary,
    surface = AgroGreenDarkSurface,
    onSurface = AgroTextDarkPrimary,
    surfaceVariant = AgroGreenDarkSurfaceVariant,
    onSurfaceVariant = AgroTextDarkSecondary,
    outline = Color(0xFF2D4E3E)
)

private val LightColorScheme = lightColorScheme(
    primary = AgroGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = AgroGreenContainer,
    onPrimaryContainer = AgroOnGreenContainer,
    secondary = HarvestAmber,
    onSecondary = Color.White,
    secondaryContainer = HarvestGoldLight,
    onSecondaryContainer = HarvestOnGold,
    tertiary = EarthTerracotta,
    background = AgroCanvasLight,
    onBackground = AgroTextPrimary,
    surface = AgroSurfaceLight,
    onSurface = AgroTextPrimary,
    surfaceVariant = AgroSurfaceVariant,
    onSurfaceVariant = AgroInputHintDark,
    outline = AgroBorderLight
)

@Composable
fun KisanMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent agricultural branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
