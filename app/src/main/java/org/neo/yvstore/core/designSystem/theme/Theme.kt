package org.neo.yvstore.core.designSystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import org.neo.yvstore.core.designSystem.color.DarkYVStoreColors
import org.neo.yvstore.core.designSystem.color.LightYVStoreColors
import org.neo.yvstore.core.designSystem.color.LocalYVStoreColors
import org.neo.yvstore.core.designSystem.color.YVStoreColors
import org.neo.yvstore.core.designSystem.color.md_theme_dark_background
import org.neo.yvstore.core.designSystem.color.md_theme_dark_error
import org.neo.yvstore.core.designSystem.color.md_theme_dark_errorContainer
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onBackground
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onError
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onErrorContainer
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onPrimary
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onPrimaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onSecondary
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onSecondaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onSurface
import org.neo.yvstore.core.designSystem.color.md_theme_dark_onSurfaceVariant
import org.neo.yvstore.core.designSystem.color.md_theme_dark_outline
import org.neo.yvstore.core.designSystem.color.md_theme_dark_primary
import org.neo.yvstore.core.designSystem.color.md_theme_dark_primaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_dark_secondary
import org.neo.yvstore.core.designSystem.color.md_theme_dark_secondaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_dark_surface
import org.neo.yvstore.core.designSystem.color.md_theme_dark_surfaceVariant
import org.neo.yvstore.core.designSystem.color.md_theme_light_background
import org.neo.yvstore.core.designSystem.color.md_theme_light_error
import org.neo.yvstore.core.designSystem.color.md_theme_light_errorContainer
import org.neo.yvstore.core.designSystem.color.md_theme_light_onBackground
import org.neo.yvstore.core.designSystem.color.md_theme_light_onError
import org.neo.yvstore.core.designSystem.color.md_theme_light_onErrorContainer
import org.neo.yvstore.core.designSystem.color.md_theme_light_onPrimary
import org.neo.yvstore.core.designSystem.color.md_theme_light_onPrimaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_light_onSecondary
import org.neo.yvstore.core.designSystem.color.md_theme_light_onSecondaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_light_onSurface
import org.neo.yvstore.core.designSystem.color.md_theme_light_onSurfaceVariant
import org.neo.yvstore.core.designSystem.color.md_theme_light_outline
import org.neo.yvstore.core.designSystem.color.md_theme_light_primary
import org.neo.yvstore.core.designSystem.color.md_theme_light_primaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_light_secondary
import org.neo.yvstore.core.designSystem.color.md_theme_light_secondaryContainer
import org.neo.yvstore.core.designSystem.color.md_theme_light_surface
import org.neo.yvstore.core.designSystem.color.md_theme_light_surfaceVariant
import org.neo.yvstore.core.designSystem.typography.Typography

// =============================================================================
// Color Schemes
// =============================================================================
val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,

    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,

    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,

    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,

    outline = md_theme_light_outline,

    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer
)

val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,

    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,

    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,

    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,

    outline = md_theme_dark_outline,

    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer
)

@Composable
fun YVStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val yVStoreColors = rememberYvStoreColors(darkTheme)

    CompositionLocalProvider(
        LocalYVStoreColors provides yVStoreColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


@Composable
fun rememberYvStoreColors(darkTheme: Boolean = isSystemInDarkTheme()): YVStoreColors {
    return remember(darkTheme) {
        if (darkTheme) DarkYVStoreColors else LightYVStoreColors
    }
}