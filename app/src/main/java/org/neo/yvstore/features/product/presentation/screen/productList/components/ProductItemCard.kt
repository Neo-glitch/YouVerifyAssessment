package org.neo.yvstore.features.product.presentation.screen.productList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.card.YVStoreElevatedCard
import org.neo.yvstore.core.ui.component.image.YVStoreImage

@Composable
fun ProductItemCard(
    name: String,
    price: String,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YVStoreElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            ProductItemImage(imageUrl = imageUrl, name = name)
            ProductItemInfo(name = name, price = price)
        }
    }
}

@Composable
private fun ProductItemImage(
    imageUrl: String,
    name: String,
    modifier: Modifier = Modifier,
) {
    YVStoreImage(
        imageUrl = imageUrl,
        contentDescription = name,
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        contentScale = ContentScale.Fit,
    )
}

@Composable
private fun ProductItemInfo(
    name: String,
    price: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = price,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemCardPreview() {
    YVStoreTheme {
        ProductItemCard(
            name = "Wireless Headphones",
            price = "$89.99",
            imageUrl = "https://picsum.photos/seed/headphones/400/400",
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemCardLongNamePreview() {
    YVStoreTheme {
        ProductItemCard(
            name = "Super Long Product Name That Will Be Truncated With Ellipsis",
            price = "$199.99",
            imageUrl = "https://picsum.photos/seed/product/400/400",
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
