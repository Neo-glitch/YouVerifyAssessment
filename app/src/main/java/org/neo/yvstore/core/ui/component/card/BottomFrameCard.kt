package org.neo.yvstore.core.ui.component.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun BottomFrameCard(modifier: Modifier = Modifier, content: @Composable (BoxScope.() -> Unit)) {
    Surface(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                    ),
                    spotColor = YVStoreTheme.colors.cardColors.bottomFrameCardShadow,
                    ambientColor = YVStoreTheme.colors.cardColors.bottomFrameCardShadow,
                ),
            contentAlignment = Alignment.Center,
            content = content,
        )
    }
}
