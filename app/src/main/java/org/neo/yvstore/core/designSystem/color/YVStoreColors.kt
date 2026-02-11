package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class YVStoreColors(
    val popUpColors: PopUpColors,
    val iconColors: IconColors,
    val textColors: TextColors,
    val navigationColors: NavigationColors,
    val cardColors: CardColors,
)

// Light theme colors
val LightYVStoreColors = YVStoreColors(
    popUpColors = LightPopUpColors,
    iconColors = LightIconColors,
    textColors = LightTextColors,
    navigationColors = LightNavigationColors,
    cardColors = LightCardColors,
)

// Dark theme colors
val DarkYVStoreColors = YVStoreColors(
    popUpColors = DarkPopUpColors,
    iconColors = DarkIconColors,
    textColors = DarkTextColors,
    navigationColors = DarkNavigationColors,
    cardColors = DarkCardColors,
)

val LocalYVStoreColors = staticCompositionLocalOf<YVStoreColors> {
    error("No YVStoreColors provided! Did you forget to use YVStoreTheme?")
}
