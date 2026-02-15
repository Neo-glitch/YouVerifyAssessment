package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import org.neo.yvstore.core.designSystem.color.token.md_theme_dark_onBackground
import org.neo.yvstore.core.designSystem.color.token.md_theme_dark_onSurfaceVariant
import org.neo.yvstore.core.designSystem.color.token.md_theme_dark_outline
import org.neo.yvstore.core.designSystem.color.token.md_theme_light_onBackground
import org.neo.yvstore.core.designSystem.color.token.md_theme_light_onSurfaceVariant
import org.neo.yvstore.core.designSystem.color.token.md_theme_light_outline

@Immutable
data class TextColors(
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textToolbarTitle: Color,
    val textEmptyErrorStateTitle: Color,
)

val LightTextColors = TextColors(
    textPrimary = md_theme_light_onBackground,
    textSecondary = md_theme_light_onSurfaceVariant,
    textTertiary = md_theme_light_outline,
    textToolbarTitle = md_theme_light_onBackground,
    textEmptyErrorStateTitle = md_theme_light_onBackground,
)

val DarkTextColors = TextColors(
    textPrimary = md_theme_dark_onBackground,
    textSecondary = md_theme_dark_onSurfaceVariant,
    textTertiary = md_theme_dark_outline,
    textToolbarTitle = md_theme_dark_onBackground,
    textEmptyErrorStateTitle = md_theme_dark_onBackground,
)
