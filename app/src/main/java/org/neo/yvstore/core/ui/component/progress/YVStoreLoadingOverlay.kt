package org.neo.yvstore.core.ui.component.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.extension.noRippleClearFocusClickable

@Composable
fun YVStoreLoadingOverlay(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.02f),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .noRippleClearFocusClickable(enabled = true, onClick = {})
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        YVStoreCircleProgressIndicator(size = 48.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreLoadingOverlayPreview() {
    YVStoreTheme {
        YVStoreLoadingOverlay()
    }
}
