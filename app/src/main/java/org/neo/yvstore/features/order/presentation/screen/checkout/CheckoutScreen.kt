package org.neo.yvstore.features.order.presentation.screen.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.card.BottomFrameCard
import org.neo.yvstore.core.ui.component.dialog.YVStoreErrorDialog
import org.neo.yvstore.core.ui.component.divider.YVStoreHorizontalDivider
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.util.ObserveAsEvents
import org.neo.yvstore.features.address.presentation.model.AddressUi
import org.neo.yvstore.features.cart.presentation.model.CartItemUi

@Composable
fun CheckoutScreen(
    addressId: String,
    onNavigateBack: () -> Unit,
    onOrderSuccess: () -> Unit,
    viewModel: CheckoutViewModel = koinViewModel { parametersOf(addressId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is CheckoutUiEvent.OrderPlaced -> onOrderSuccess()
        }
    }

    if (uiState.placeOrderState is PlaceOrderState.Error) {
        YVStoreErrorDialog(
            title = "Order Failed",
            description = (uiState.placeOrderState as PlaceOrderState.Error).message,
            onDismiss = viewModel::dismissError,
            onPrimaryButtonClick = viewModel::dismissError,
            primaryButtonText = "OK",
        )
    }

    CheckoutScreen(
        cartItems = uiState.cartItems,
        address = uiState.address,
        subtotal = uiState.formattedSubtotal,
        deliveryFee = uiState.formattedDeliveryFee,
        total = uiState.formattedTotal,
        loadState = uiState.loadState,
        isPlacingOrder = uiState.placeOrderState is PlaceOrderState.Placing,
        onNavigateBack = onNavigateBack,
        onPlaceOrder = viewModel::placeOrder,
    )
}

@Composable
private fun CheckoutScreen(
    cartItems: List<CartItemUi>,
    address: AddressUi?,
    subtotal: String,
    deliveryFee: String,
    total: String,
    loadState: CheckoutLoadState,
    isPlacingOrder: Boolean,
    onNavigateBack: () -> Unit,
    onPlaceOrder: () -> Unit,
) {
    YVStoreScaffold(
        topBar = {
            YVStoreTopBar(
                title = "Checkout",
                onNavigationClick = onNavigateBack,
                isCenteredAligned = true,
            )
        },
        bottomBar = {
            val displayCheckoutBottomBar = loadState is CheckoutLoadState.Loaded && cartItems.isNotEmpty()
            if (displayCheckoutBottomBar) {
                CheckoutBottomBar(
                    subtotal = subtotal,
                    deliveryFee = deliveryFee,
                    total = total,
                    isPlacingOrder = isPlacingOrder,
                    onPlaceOrder = onPlaceOrder,
                )
            }
        }
    ) { paddingValues ->
        when (loadState) {
            is CheckoutLoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    YVStoreCircleProgressIndicator(size = 48.dp)
                }
            }

            is CheckoutLoadState.Error -> {
                YVStoreEmptyErrorStateView(
                    image = org.neo.yvstore.R.drawable.ic_empty_cart,
                    title = "Error loading checkout",
                    description = loadState.message,
                    modifier = Modifier.padding(paddingValues),
                )
            }

            is CheckoutLoadState.Loaded -> {
                CheckoutContent(
                    cartItems = cartItems,
                    address = address,
                    paddingValues = paddingValues,
                )
            }
        }
    }
}

@Composable
private fun CheckoutBottomBar(
    subtotal: String,
    deliveryFee: String,
    total: String,
    isPlacingOrder: Boolean,
    onPlaceOrder: () -> Unit,
) {
    BottomFrameCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            OrderSummary(
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                total = total,
            )
            Spacer(modifier = Modifier.height(16.dp))
            YVStorePrimaryButton(
                text = "Place Order",
                onClick = onPlaceOrder,
                modifier = Modifier.fillMaxWidth(),
                loading = isPlacingOrder,
                enabled = !isPlacingOrder,
            )
        }
    }
}

@Composable
private fun CheckoutContent(
    cartItems: List<CartItemUi>,
    address: AddressUi?,
    paddingValues: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Order Items",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        itemsIndexed(
            items = cartItems,
            key = { _, item -> item.id }
        ) { index, item ->
            CartItem(item = item)
            if (index < cartItems.lastIndex) {
                YVStoreHorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }

        if (address != null) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AddressSection(address = address)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CartItem(item: CartItemUi) {
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
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.formattedPrice,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Qty: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}

@Composable
private fun AddressSection(address: AddressUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Delivery Address",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = address.formattedAddress,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }
}

@Composable
private fun OrderSummary(
    subtotal: String,
    deliveryFee: String,
    total: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SummaryRow(label = "Subtotal:", value = subtotal)
        Spacer(modifier = Modifier.height(12.dp))
        SummaryRow(label = "Delivery Fee:", value = deliveryFee)
        Spacer(modifier = Modifier.height(12.dp))
        SummaryRow(label = "Total:", value = total, isBold = true)
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
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isBold) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutScreenPreview() {
    YVStoreTheme {
        CheckoutScreen(
            cartItems = listOf(
                CartItemUi(
                    id = 1L,
                    name = "Xbox Series X",
                    price = 570.0,
                    imageUrl = "",
                    quantity = 1,
                ),
                CartItemUi(
                    id = 2L,
                    name = "PlayStation 5",
                    price = 499.99,
                    imageUrl = "",
                    quantity = 2,
                ),
            ),
            address = AddressUi(
                id = "1",
                streetAddress = "123 Main Street",
                city = "San Francisco",
                state = "California",
                country = "United States",
                formattedAddress = "123 Main Street, San Francisco, California, United States",
            ),
            subtotal = "$1,569.98",
            deliveryFee = "$5.00",
            total = "$1,574.98",
            loadState = CheckoutLoadState.Loaded,
            isPlacingOrder = false,
            onNavigateBack = {},
            onPlaceOrder = {},
        )
    }
}
