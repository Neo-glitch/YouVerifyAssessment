package org.neo.yvstore.core.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun DialogIcon(icon: Int, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(80.dp),
        painter = painterResource(icon),
        contentDescription = null,
    )
}

@Composable
fun DialogTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = YVStoreTheme.colors.textColors.textPrimary,
        ),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun DefaultDialogDescription(description: String, modifier: Modifier = Modifier) {
    Text(
        text = description,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 14.sp,
            color = YVStoreTheme.colors.textColors.textSecondary,
        ),
        textAlign = TextAlign.Center,
    )
}
