package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import org.neo.yvstore.core.designSystem.color.token.md_theme_dark_cardShadow
import org.neo.yvstore.core.designSystem.color.token.md_theme_light_cardShadow

@Immutable
data class CardColors(
    val bottomFrameCardShadow: Color,
)

val LightCardColors = CardColors(
    bottomFrameCardShadow = md_theme_light_cardShadow,
)

val DarkCardColors = CardColors(
    bottomFrameCardShadow = md_theme_dark_cardShadow,
)
