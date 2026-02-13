package org.neo.yvstore.features.product.presentation.screen.productList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun CartIconButton(
    hasCartItems: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                )
                .clip(CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_nav_cart),
                contentDescription = "Shopping cart",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        if (hasCartItems) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-3).dp, y = 3.dp)
                    .size(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartIconButtonEmptyPreview() {
    YVStoreTheme {
        CartIconButton(
            hasCartItems = false,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartIconButtonWithItemsPreview() {
    YVStoreTheme {
        CartIconButton(
            hasCartItems = true,
            onClick = {},
        )
    }
}
