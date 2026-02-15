package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import org.neo.yvstore.core.designSystem.color.token.md_theme_dark_outline
import org.neo.yvstore.core.designSystem.color.token.md_theme_light_outline

@Immutable
data class IconColors(
    val inputPrefixIcon: Color,
    val inputSuffixIcon: Color,
)

val LightIconColors = IconColors(
    inputPrefixIcon = md_theme_light_outline,
    inputSuffixIcon = md_theme_light_outline,
)

val DarkIconColors = IconColors(
    inputPrefixIcon = md_theme_dark_outline,
    inputSuffixIcon = md_theme_dark_outline,
)
