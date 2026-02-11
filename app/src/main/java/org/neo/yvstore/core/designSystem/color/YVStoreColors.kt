package org.neo.yvstore.core.designSystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class YVStoreColors(
    val popUpColors: PopUpColors
)

// Light theme colors
val LightYVStoreColors = YVStoreColors(
    popUpColors = LightPopUpColors
)

// Dark theme colors
val DarkYVStoreColors = YVStoreColors(
    popUpColors = DarkPopUpColors
)

val LocalYVStoreColors = staticCompositionLocalOf<YVStoreColors> {
    error("No YVStoreColors provided! Did you forget to use YVStoreTheme?")
}
