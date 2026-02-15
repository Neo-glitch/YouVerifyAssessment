package org.neo.yvstore.features.order.presentation.screen.orderSuccess

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold

@Composable
fun OrderSuccessScreen(
    onNavigateBack: () -> Unit,
) {
    BackHandler(onBack = onNavigateBack)

    YVStoreScaffold(
        topBar = {
            YVStoreTopBar(
                title = "",
                onNavigationClick = onNavigateBack,
                isCenteredAligned = true,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val primaryColor = MaterialTheme.colorScheme.primary

            SuccessIcon(primaryColor)

            Spacer(modifier = Modifier.height(16.dp))

            SuccessTitle(primaryColor)

            Spacer(modifier = Modifier.height(12.dp))

            SuccessMessage()

            Spacer(modifier = Modifier.height(32.dp))

            YVStorePrimaryButton(
                text = "Continue Shopping",
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SuccessIcon(color: Color) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .border(
                width = 3.dp,
                color = color,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = "Order successful",
            modifier = Modifier.size(60.dp),
            tint = color,
        )
    }
}

@Composable
private fun SuccessTitle(color: Color) {
    Text(
        text = "Purchase Completed",
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.SemiBold,
            color = color,
        ),
    )
}

@Composable
private fun SuccessMessage() {
    Text(
        text = "Thank you for purchase! Your order has been successfully placed.",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderSuccessScreenPreview() {
    YVStoreTheme {
        OrderSuccessScreen(
            onNavigateBack = {},
        )
    }
}
