package com.orbix.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = WhiteColor,
    primaryContainer = OrangeContainerDark,
    onPrimaryContainer = OrangePrimary,
    secondary = BluePrimary,
    onSecondary = WhiteColor,
    secondaryContainer = BlueContainerDark,
    onSecondaryContainer = BluePrimary,
    tertiary = GoldTertiary,
    onTertiary = WhiteColor,
    tertiaryContainer = GoldContainerDark,
    onTertiaryContainer = GoldTertiary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
)

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = WhiteColor,
    primaryContainer = OrangeContainerLight,
    onPrimaryContainer = OrangePrimary,
    secondary = BluePrimary,
    onSecondary = WhiteColor,
    secondaryContainer = BlueContainerLight,
    onSecondaryContainer = BluePrimary,
    tertiary = GoldTertiary,
    onTertiary = WhiteColor,
    tertiaryContainer = GoldContainerLight,
    onTertiaryContainer = GoldTertiary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    error = LightError,
    onError = WhiteColor,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
)

@Composable
fun OrbixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
