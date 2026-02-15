package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import org.neo.yvstore.core.designSystem.color.token.md_theme_dark_appbarIconBackground
import org.neo.yvstore.core.designSystem.color.token.md_theme_dark_onBackground
import org.neo.yvstore.core.designSystem.color.token.md_theme_light_appbarIconBackground
import org.neo.yvstore.core.designSystem.color.token.md_theme_light_onBackground

@Immutable
data class NavigationColors(
    val appbarIconsBackground: Color,
    val navigationIcon: Color,
)

val LightNavigationColors = NavigationColors(
    appbarIconsBackground = md_theme_light_appbarIconBackground,
    navigationIcon = md_theme_light_onBackground,
)

val DarkNavigationColors = NavigationColors(
    appbarIconsBackground = md_theme_dark_appbarIconBackground,
    navigationIcon = md_theme_dark_onBackground,
)
