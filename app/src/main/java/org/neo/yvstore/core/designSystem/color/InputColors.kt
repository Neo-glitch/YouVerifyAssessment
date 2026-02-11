package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class IconColors(
    val inputPrefixIcon: Color,
    val inputSuffixIcon: Color,
)

@Immutable
data class TextColors(
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textToolbarTitle: Color,
    val textEmptyErrorStateTitle: Color,
)

@Immutable
data class NavigationColors(
    val appbarIconsBackground: Color,
    val navigationIcon: Color,
)

@Immutable
data class CardColors(
    val bottomFrameCardShadow: Color,
)

// Light theme icon colors
val LightIconColors = IconColors(
    inputPrefixIcon = Color(0xFF6F797B),
    inputSuffixIcon = Color(0xFF6F797B),
)

// Dark theme icon colors
val DarkIconColors = IconColors(
    inputPrefixIcon = Color(0xFF899394),
    inputSuffixIcon = Color(0xFF899394),
)

// Light theme text colors
val LightTextColors = TextColors(
    textPrimary = Color(0xFF191C1D),
    textSecondary = Color(0xFF3F484A),
    textTertiary = Color(0xFF6F797B),
    textToolbarTitle = Color(0xFF191C1D),
    textEmptyErrorStateTitle = Color(0xFF191C1D),
)

// Dark theme text colors
val DarkTextColors = TextColors(
    textPrimary = Color(0xFFE1E3E3),
    textSecondary = Color(0xFFBFC8CA),
    textTertiary = Color(0xFF899394),
    textToolbarTitle = Color(0xFFE1E3E3),
    textEmptyErrorStateTitle = Color(0xFFE1E3E3),
)

// Light theme navigation colors
val LightNavigationColors = NavigationColors(
    appbarIconsBackground = Color(0xFFF5F5F5),
    navigationIcon = Color(0xFF191C1D),
)

// Dark theme navigation colors
val DarkNavigationColors = NavigationColors(
    appbarIconsBackground = Color(0xFF2C2C2C),
    navigationIcon = Color(0xFFE1E3E3),
)

// Light theme card colors
val LightCardColors = CardColors(
    bottomFrameCardShadow = Color(0x1A000000), // Black with 10% alpha
)

// Dark theme card colors
val DarkCardColors = CardColors(
    bottomFrameCardShadow = Color(0x33000000), // Black with 20% alpha
)

