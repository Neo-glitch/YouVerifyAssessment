package org.neo.yvstore.features.product.presentation.screen.productList

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStoreTextButton
import org.neo.yvstore.core.ui.component.grid.NonlazyGrid
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.util.ObserveAsEvents
import org.neo.yvstore.features.product.presentation.model.ProductItemUi
import org.neo.yvstore.features.product.presentation.model.ProductListLoadState
import org.neo.yvstore.features.product.presentation.model.ProductListUiEvent
import org.neo.yvstore.features.product.presentation.screen.productList.components.CartIconButton
import org.neo.yvstore.features.product.presentation.screen.productList.components.ProductCard
import org.neo.yvstore.features.product.presentation.screen.productList.components.PromoBanner
import org.neo.yvstore.features.product.presentation.screen.productList.components.SearchBarPlaceholder

@Composable
fun HomeProductListScreen(
    onNavigateToCart: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onViewAllClick: () -> Unit,
    viewModel: HomeProductListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is ProductListUiEvent.ShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    HomeProductListScreen(
        products = uiState.products,
        loadState = uiState.loadState,
        hasCartItems = false, // TODO: Connect to cart state later
        promoTitle = "Clearance Sales",
        promoDiscountText = "Up to 50%",
        promoImageUrl = "https://picsum.photos/seed/promo/200/200",
        onNavigateToCart = onNavigateToCart,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToProductDetails = onNavigateToProductDetails,
        onViewAllClick = onViewAllClick,
    )
}

@Composable
private fun HomeProductListScreen(
    products: List<ProductItemUi>,
    loadState: ProductListLoadState,
    hasCartItems: Boolean,
    promoTitle: String,
    promoDiscountText: String,
    promoImageUrl: String,
    onNavigateToCart: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onViewAllClick: () -> Unit,
) {
    YVStoreScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderRow(
                hasCartItems = hasCartItems,
                onCartClick = onNavigateToCart,
            )

            Spacer(modifier = Modifier.height(16.dp))
            SearchBarPlaceholder(
                onClick = onNavigateToSearch,
            )

            // Show loading or error state when products list is empty
            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    when (loadState) {
                        ProductListLoadState.Loading -> {
                            YVStoreCircleProgressIndicator(size = 48.dp)
                        }
                        is ProductListLoadState.Error -> {
                            YVStoreEmptyErrorStateView(
                                image = android.R.drawable.ic_dialog_alert,
                                title = "Unable to Load Products",
                                description = loadState.message,
                            )
                        }
                        ProductListLoadState.Loaded -> {
                            // Empty state with loaded status (no products available)
                            YVStoreEmptyErrorStateView(
                                image = android.R.drawable.ic_dialog_info,
                                title = "No Products Available",
                                description = "Check back later for new products.",
                            )
                        }
                    }
                }
            } else {
                // Show content when products are available
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    PromoBanner(
                        title = promoTitle,
                        discountText = promoDiscountText,
                        imageUrl = promoImageUrl,
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    ProductsSectionHeader(
                        title = "Products",
                        onViewAllClick = onViewAllClick,
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    ProductsGrid(
                        products = products,
                        onProductClick = onNavigateToProductDetails,
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun HeaderRow(
    hasCartItems: Boolean,
    onCartClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )

        CartIconButton(
            hasCartItems = hasCartItems,
            onClick = onCartClick,
        )
    }
}

@Composable
private fun ProductsSectionHeader(
    title: String,
    onViewAllClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        YVStoreTextButton(
            onClick = onViewAllClick,
            text = "View All",
        )
    }
}

@Composable
private fun ProductsGrid(
    products: List<ProductItemUi>,
    onProductClick: (String) -> Unit,
) {
    NonlazyGrid(
        columns = 2,
        itemCount = products.size,
        horizontalSpacing = 12.dp,
        verticalSpacing = 16.dp,
    ) { index ->
        val product = products[index]
        ProductCard(
            name = product.name,
            price = product.price,
            imageUrl = product.imageUrl,
            onClick = { onProductClick(product.id) },
        )
    }
}

private val placeholderProducts = listOf(
    ProductItemUi(
        id = "1",
        name = "Wireless Headphones",
        price = "$89.99",
        imageUrl = "https://picsum.photos/seed/headphones/400/400",
    ),
    ProductItemUi(
        id = "2",
        name = "Smart Watch",
        price = "$199.99",
        imageUrl = "https://picsum.photos/seed/watch/400/400",
    ),
    ProductItemUi(
        id = "3",
        name = "Laptop Stand",
        price = "$45.00",
        imageUrl = "https://picsum.photos/seed/stand/400/400",
    ),
    ProductItemUi(
        id = "4",
        name = "USB-C Hub",
        price = "$29.99",
        imageUrl = "https://picsum.photos/seed/hub/400/400",
    ),
    ProductItemUi(
        id = "5",
        name = "Mechanical Keyboard",
        price = "$129.99",
        imageUrl = "https://picsum.photos/seed/keyboard/400/400",
    ),
    ProductItemUi(
        id = "6",
        name = "Wireless Mouse",
        price = "$39.99",
        imageUrl = "https://picsum.photos/seed/mouse/400/400",
    ),
    ProductItemUi(
        id = "7",
        name = "Phone Case",
        price = "$19.99",
        imageUrl = "https://picsum.photos/seed/case/400/400",
    ),
    ProductItemUi(
        id = "8",
        name = "Portable Charger",
        price = "$34.99",
        imageUrl = "https://picsum.photos/seed/charger/400/400",
    ),
    ProductItemUi(
        id = "9",
        name = "Bluetooth Speaker",
        price = "$59.99",
        imageUrl = "https://picsum.photos/seed/speaker/400/400",
    ),
    ProductItemUi(
        id = "10",
        name = "Webcam HD",
        price = "$79.99",
        imageUrl = "https://picsum.photos/seed/webcam/400/400",
    ),
)

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenPreview() {
    YVStoreTheme {
        HomeProductListScreen(
            products = placeholderProducts,
            loadState = ProductListLoadState.Loaded,
            hasCartItems = false,
            promoTitle = "Clearance Sales",
            promoDiscountText = "Up to 50%",
            promoImageUrl = "https://picsum.photos/seed/promo/200/200",
            onNavigateToCart = {},
            onNavigateToSearch = {},
            onNavigateToProductDetails = {},
            onViewAllClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenWithCartItemsPreview() {
    YVStoreTheme {
        HomeProductListScreen(
            products = placeholderProducts,
            loadState = ProductListLoadState.Loaded,
            hasCartItems = true,
            promoTitle = "Clearance Sales",
            promoDiscountText = "Up to 50%",
            promoImageUrl = "https://picsum.photos/seed/promo/200/200",
            onNavigateToCart = {},
            onNavigateToSearch = {},
            onNavigateToProductDetails = {},
            onViewAllClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenLoadingPreview() {
    YVStoreTheme {
        HomeProductListScreen(
            products = emptyList(),
            loadState = ProductListLoadState.Loading,
            hasCartItems = false,
            promoTitle = "Clearance Sales",
            promoDiscountText = "Up to 50%",
            promoImageUrl = "https://picsum.photos/seed/promo/200/200",
            onNavigateToCart = {},
            onNavigateToSearch = {},
            onNavigateToProductDetails = {},
            onViewAllClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenErrorPreview() {
    YVStoreTheme {
        HomeProductListScreen(
            products = emptyList(),
            loadState = ProductListLoadState.Error("Failed to load products. Please check your internet connection."),
            hasCartItems = false,
            promoTitle = "Clearance Sales",
            promoDiscountText = "Up to 50%",
            promoImageUrl = "https://picsum.photos/seed/promo/200/200",
            onNavigateToCart = {},
            onNavigateToSearch = {},
            onNavigateToProductDetails = {},
            onViewAllClick = {},
        )
    }
}
