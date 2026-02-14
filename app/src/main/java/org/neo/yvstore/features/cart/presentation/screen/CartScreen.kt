package org.neo.yvstore.features.cart.presentation.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.card.BottomFrameCard
import org.neo.yvstore.core.ui.component.dialog.DefaultDialogDescription
import org.neo.yvstore.core.ui.component.dialog.YVStoreActionDialog
import org.neo.yvstore.core.ui.component.divider.YVStoreHorizontalDivider
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.features.cart.presentation.model.CartItemUi

@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onCheckout: () -> Unit,
    viewModel: CartScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CartScreen(
        cartItems = uiState.cartItems,
        subtotal = uiState.formattedSubtotal,
        deliveryFee = uiState.formattedDeliveryFee,
        total = uiState.formattedTotal,
        loadState = uiState.loadState,
        showClearCartDialog = uiState.showClearCartDialog,
        onNavigateBack = onNavigateBack,
        onCheckout = onCheckout,
        onIncrementQuantity = viewModel::onIncrementQuantity,
        onDecrementQuantity = viewModel::onDecrementQuantity,
        onRemoveItem = viewModel::onRemoveItem,
        onShowClearCartDialog = viewModel::onShowClearCartDialog,
        onDismissClearCartDialog = viewModel::onDismissClearCartDialog,
        onConfirmClearCart = viewModel::onConfirmClearCart,
    )
}

@Composable
private fun CartScreen(
    cartItems: List<CartItemUi>,
    subtotal: String,
    deliveryFee: String,
    total: String,
    loadState: CartScreenLoadState,
    showClearCartDialog: Boolean,
    onNavigateBack: () -> Unit,
    onCheckout: () -> Unit,
    onIncrementQuantity: (Long) -> Unit,
    onDecrementQuantity: (Long) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onShowClearCartDialog: () -> Unit,
    onDismissClearCartDialog: () -> Unit,
    onConfirmClearCart: () -> Unit,
) {
    YVStoreScaffold(
        topBar = {
            YVStoreTopBar(
                title = "My cart",
                onNavigationClick = onNavigateBack,
                isCenteredAligned = true,
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = onShowClearCartDialog) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = "Clear cart",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                BottomFrameCard {
                    YVStorePrimaryButton(
                        text = "Checkout",
                        onClick = onCheckout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }
        }
    ) { paddingValues ->
        when (loadState) {
            is CartScreenLoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    YVStoreCircleProgressIndicator(size = 48.dp)
                }
            }
            is CartScreenLoadState.Error -> {
                YVStoreEmptyErrorStateView(
                    image = R.drawable.ic_empty_cart,
                    title = "Error loading cart",
                    description = loadState.message,
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is CartScreenLoadState.Loaded -> {
                if (cartItems.isEmpty()) {
                    YVStoreEmptyErrorStateView(
                        image = R.drawable.ic_empty_cart,
                        title = "Your cart is empty",
                        description = "Add items to your cart to see them here",
                        modifier = Modifier.padding(paddingValues),
                    )
                } else {
                    CartContentList(
                        cartItems = cartItems,
                        subtotal = subtotal,
                        deliveryFee = deliveryFee,
                        total = total,
                        onIncrementQuantity = onIncrementQuantity,
                        onDecrementQuantity = onDecrementQuantity,
                        onRemoveItem = onRemoveItem,
                        paddingValues = paddingValues,
                    )
                }
            }
        }
    }

    ClearCartDialog(
        showDialog = showClearCartDialog,
        onDismiss = onDismissClearCartDialog,
        onConfirm = onConfirmClearCart,
    )
}

@Composable
private fun CartContentList(
    cartItems: List<CartItemUi>,
    subtotal: String,
    deliveryFee: String,
    total: String,
    onIncrementQuantity: (Long) -> Unit,
    onDecrementQuantity: (Long) -> Unit,
    onRemoveItem: (Long) -> Unit,
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(
            items = cartItems,
            key = { it.id }
        ) { item ->
            CartItemRow(
                item = item,
                onIncrementQuantity = { onIncrementQuantity(item.id) },
                onDecrementQuantity = { onDecrementQuantity(item.id) },
                onRemoveItem = { onRemoveItem(item.id) },
            )
            YVStoreHorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        item {
            OrderSummary(
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                total = total,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ClearCartDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog) {
        YVStoreActionDialog(
            icon = R.drawable.ic_delete,
            title = "Clear Cart",
            description = {
                DefaultDialogDescription(
                    description = "Are you sure you want to remove all items from your cart?"
                )
            },
            onDismiss = onDismiss,
            onPrimaryButtonClick = {
                onConfirm()
                onDismiss()
            },
            primaryButtonText = "Clear Cart",
            onSecondaryButtonClick = onDismiss,
            secondaryButtonText = "Cancel",
            iconColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
        )
    }
}

@Composable
private fun CartItemRow(
    item: CartItemUi,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onRemoveItem: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Fit,
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.formattedPrice,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }

                IconButton(
                    onClick = onRemoveItem,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Remove item",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            QuantitySelector(
                quantity = item.quantity,
                onIncrement = onIncrementQuantity,
                onDecrement = onDecrementQuantity,
            )
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
                .size(32.dp)
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
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
        }

        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Box(
            modifier = Modifier
                .size(32.dp)
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
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    }
}

@Composable
private fun OrderSummary(
    subtotal: String,
    deliveryFee: String,
    total: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SummaryRow(
            label = "Subtotal:",
            value = subtotal,
        )

        Spacer(modifier = Modifier.height(12.dp))

        SummaryRow(
            label = "Delivery Fee:",
            value = deliveryFee,
        )

        Spacer(modifier = Modifier.height(12.dp))

        SummaryRow(
            label = "Total:",
            value = total,
            isBold = true,
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    isBold: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            ),
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenPopulatedPreview() {
    YVStoreTheme {
        CartScreen(
            cartItems = listOf(
                CartItemUi(
                    id = 1L,
                    name = "Xbox series X",
                    price = 570.0,
                    imageUrl = "https://picsum.photos/seed/xbox/400/400",
                    quantity = 1,
                ),
                CartItemUi(
                    id = 2L,
                    name = "PlayStation 5",
                    price = 499.99,
                    imageUrl = "https://picsum.photos/seed/ps5/400/400",
                    quantity = 1,
                ),
            ),
            subtotal = "$1,069.99",
            deliveryFee = "$5.00",
            total = "$1,074.99",
            loadState = CartScreenLoadState.Loaded,
            showClearCartDialog = false,
            onNavigateBack = {},
            onCheckout = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onRemoveItem = {},
            onShowClearCartDialog = {},
            onDismissClearCartDialog = {},
            onConfirmClearCart = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenEmptyPreview() {
    YVStoreTheme {
        CartScreen(
            cartItems = emptyList(),
            subtotal = "$0.00",
            deliveryFee = "$5.00",
            total = "$5.00",
            loadState = CartScreenLoadState.Loaded,
            showClearCartDialog = false,
            onNavigateBack = {},
            onCheckout = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onRemoveItem = {},
            onShowClearCartDialog = {},
            onDismissClearCartDialog = {},
            onConfirmClearCart = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenClearDialogPreview() {
    YVStoreTheme {
        CartScreen(
            cartItems = listOf(
                CartItemUi(
                    id = 1L,
                    name = "Xbox series X",
                    price = 570.0,
                    imageUrl = "https://picsum.photos/seed/xbox/400/400",
                    quantity = 1,
                ),
            ),
            subtotal = "$570.00",
            deliveryFee = "$5.00",
            total = "$575.00",
            loadState = CartScreenLoadState.Loaded,
            showClearCartDialog = true,
            onNavigateBack = {},
            onCheckout = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onRemoveItem = {},
            onShowClearCartDialog = {},
            onDismissClearCartDialog = {},
            onConfirmClearCart = {},
        )
    }
}
