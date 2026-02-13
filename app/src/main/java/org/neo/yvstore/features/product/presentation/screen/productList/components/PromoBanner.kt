package org.neo.yvstore.features.product.presentation.screen.productList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun PromoBanner(
    title: String,
    discountText: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(16.dp)
            )
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = discountText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            AsyncImage(
                model = imageUrl,
                contentDescription = "Promotional product",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PromoBannerPreview() {
    YVStoreTheme {
        PromoBanner(
            title = "Clearance Sales",
            discountText = "Up to 50%",
            imageUrl = "https://picsum.photos/seed/promo/200/200",
        )
    }
}
