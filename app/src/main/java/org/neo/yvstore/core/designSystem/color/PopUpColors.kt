package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class PopUpColors(
    val warning: Color,
    val success: Color
)

val LightPopUpColors = PopUpColors(
    warning = md_theme_light_warning,
    success = md_theme_light_success
)

val DarkPopUpColors = PopUpColors(
    warning = md_theme_dark_warning,
    success = md_theme_dark_success
)