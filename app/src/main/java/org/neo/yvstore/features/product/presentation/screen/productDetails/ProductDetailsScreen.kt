package org.neo.yvstore.features.product.presentation.screen.productDetails

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.card.BottomFrameCard
import org.neo.yvstore.core.ui.component.dialog.YVStoreErrorDialog
import org.neo.yvstore.core.ui.component.divider.YVStoreHorizontalDivider
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.util.ObserveAsEvents
import org.neo.yvstore.features.product.presentation.model.ProductDetailsUi
import kotlin.math.roundToInt

@Composable
fun ProductDetailsScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailsViewModel = koinViewModel(),
) {
    viewModel.init(productId)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is ProductDetailsUiEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ProductDetailsScreen(
        product = uiState.product,
        quantity = uiState.quantity,
        totalPrice = uiState.formattedTotalPrice,
        loadState = uiState.loadState,
        onNavigateBack = onNavigateBack,
        onAddToCart = viewModel::onAddToCart,
        onIncrementQuantity = viewModel::onIncrementQuantity,
        onDecrementQuantity = viewModel::onDecrementQuantity,
    )
}

@Composable
private fun ProductDetailsScreen(
    product: ProductDetailsUi?,
    quantity: Int,
    totalPrice: String,
    loadState: ProductDetailsLoadState,
    onNavigateBack: () -> Unit,
    onAddToCart: () -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
) {
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (loadState is ProductDetailsLoadState.Error) {
        showErrorDialog = true
        errorMessage = loadState.message
    }

    if (showErrorDialog) {
        YVStoreErrorDialog(
            title = "Error",
            description = errorMessage,
            onDismiss = {
                showErrorDialog = false
                onNavigateBack()
            },
            onPrimaryButtonClick = { },
            primaryButtonText = "OK",
        )
    }

    YVStoreScaffold(
        topBar = {
            YVStoreTopBar(
                title = "Details",
                onNavigationClick = onNavigateBack,
                isCenteredAligned = true,
            )
        },
        bottomBar = {
            if (loadState is ProductDetailsLoadState.Loaded) {
                BottomFrameCard {
                    CartActionBar(
                        product = product,
                        quantity = quantity,
                        totalPrice = totalPrice,
                        onAddToCart = onAddToCart,
                        onIncrementQuantity = onIncrementQuantity,
                        onDecrementQuantity = onDecrementQuantity,
                    )
                }
            }
        }
    ) { paddingValues ->
        when (loadState) {
            is ProductDetailsLoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    YVStoreCircleProgressIndicator(size = 48.dp)
                }
            }
            is ProductDetailsLoadState.Loaded -> {
                if (product != null) {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ProductImageSection(imageUrl = product.imageUrl)

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ProductInfoSection(
                                name = product.name,
                                description = product.description,
                                price = product.formattedPrice,
                                rating = product.rating,
                                reviewCount = product.reviewCount,
                            )

                            ProductDetailsSection(details = product.details)

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
            is ProductDetailsLoadState.Error -> {
                // Error dialog is already shown above
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ProductImageSection(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Product image",
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentScale = ContentScale.FillBounds,
    )
}

@Composable
private fun ProductInfoSection(
    name: String,
    description: String,
    price: String,
    rating: Double,
    reviewCount: Int,
) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = name,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.SemiBold,
        ),
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = price,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        ),
    )

    Spacer(modifier = Modifier.height(8.dp))

    ReviewRow(
        rating = rating,
        reviewCount = reviewCount,
    )
}

@Composable
private fun ReviewRow(rating: Double, reviewCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val fullStars = rating.roundToInt()
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (index < fullStars) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                },
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "($reviewCount reviews)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProductDetailsSection(details: String) {
    Spacer(modifier = Modifier.height(16.dp))

    YVStoreHorizontalDivider()

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Product Details",
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
        ),
    )

    Spacer(modifier = Modifier.height(8.dp))

    YVStoreHorizontalDivider()

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = details,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun CartActionBar(
    product: ProductDetailsUi?,
    quantity: Int,
    totalPrice: String,
    onAddToCart: () -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "Total Price",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            AnimatedContent(
                targetState = if (quantity == 0) product?.formattedPrice ?: "$0.00" else totalPrice,
                label = "price_animation"
            ) { price ->
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
        }

        AnimatedContent(
            targetState = quantity == 0,
            label = "cart_action_animation"
        ) { showAddToCartButton ->
            if (showAddToCartButton) {
                YVStorePrimaryButton(
                    text = "Add to Cart",
                    onClick = onAddToCart,
                )
            } else {
                QuantitySelector(
                    quantity = quantity,
                    onIncrement = onIncrementQuantity,
                    onDecrement = onDecrementQuantity,
                )
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape,
                )
                .clickable(onClick = onDecrement),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "−",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
        }

        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                )
                .clickable(onClick = onIncrement),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenAddToCartPreview() {
    YVStoreTheme {
        ProductDetailsScreen(
            product = ProductDetailsUi(
                id = "1",
                name = "Wireless Headphones",
                description = "Premium sound quality with active noise cancellation. Experience music like never before with deep bass and crystal clear highs.",
                price = 89.99,
                imageUrl = "https://picsum.photos/seed/headphones/400/400",
                rating = 4.5,
                reviewCount = 128,
                details = "These wireless headphones feature Bluetooth 5.0 connectivity, 30-hour battery life, and comfortable over-ear design. Perfect for music lovers, gamers, and professionals who demand high-quality audio. Includes a carrying case and audio cable for wired connection."
            ),
            quantity = 0,
            totalPrice = "$0.00",
            loadState = ProductDetailsLoadState.Loaded,
            onNavigateBack = {},
            onAddToCart = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenQuantitySelectorPreview() {
    YVStoreTheme {
        ProductDetailsScreen(
            product = ProductDetailsUi(
                id = "1",
                name = "Wireless Headphones",
                description = "Premium sound quality with active noise cancellation. Experience music like never before with deep bass and crystal clear highs.",
                price = 89.99,
                imageUrl = "https://picsum.photos/seed/headphones/400/400",
                rating = 4.5,
                reviewCount = 128,
                details = "These wireless headphones feature Bluetooth 5.0 connectivity, 30-hour battery life, and comfortable over-ear design. Perfect for music lovers, gamers, and professionals who demand high-quality audio. Includes a carrying case and audio cable for wired connection."
            ),
            quantity = 3,
            totalPrice = "$269.97",
            loadState = ProductDetailsLoadState.Loaded,
            onNavigateBack = {},
            onAddToCart = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
        )
    }
}
