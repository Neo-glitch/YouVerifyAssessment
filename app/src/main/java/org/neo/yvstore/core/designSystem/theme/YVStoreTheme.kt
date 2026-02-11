package org.neo.yvstore.core.designSystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import org.neo.yvstore.core.designSystem.color.LocalYVStoreColors
import org.neo.yvstore.core.designSystem.color.YVStoreColors

object YVStoreTheme {
    val colors: YVStoreColors
        @Composable
        @ReadOnlyComposable
        get() = LocalYVStoreColors.current

}