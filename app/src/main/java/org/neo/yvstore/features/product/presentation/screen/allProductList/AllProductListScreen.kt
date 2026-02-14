package org.neo.yvstore.features.product.presentation.screen.allProductList

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.util.ObserveAsEvents
import org.neo.yvstore.features.product.presentation.model.ProductItemUi
import org.neo.yvstore.features.product.presentation.screen.productList.components.ProductCard

@Composable
fun AllProductListScreen(
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: AllProductListViewModel = koinViewModel(),
) {
    viewModel.initialize()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is AllProductListUiEvent.ShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    AllProductListScreen(
        products = uiState.products,
        loadState = uiState.loadState,
        onBackClick = onBackClick,
        onProductClick = onProductClick,
    )
}


@Composable
private fun AllProductListScreen(
    products: List<ProductItemUi>,
    loadState: AllProductListLoadState,
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit,
) {
    YVStoreScaffold(
        topBar = {
            YVStoreTopBar(
                title = "All Products",
                onNavigationClick = onBackClick,
            )
        }
    ) { paddingValues ->
        if (products.isEmpty()) {
            EmptyStateContent(
                loadState = loadState,
                paddingValues = paddingValues,
            )
        } else {
            ProductsGrid(
                products = products,
                onProductClick = onProductClick,
                paddingValues = paddingValues,
            )
        }
    }
}

@Composable
private fun EmptyStateContent(
    loadState: AllProductListLoadState,
    paddingValues: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        when (loadState) {
            AllProductListLoadState.Loading -> {
                YVStoreCircleProgressIndicator(size = 48.dp)
            }
            is AllProductListLoadState.Error -> {
                YVStoreEmptyErrorStateView(
                    image = android.R.drawable.ic_dialog_alert,
                    title = "Unable to Load Products",
                    description = loadState.message,
                )
            }
            AllProductListLoadState.Loaded -> {
                YVStoreEmptyErrorStateView(
                    image = android.R.drawable.ic_dialog_info,
                    title = "No Products Available",
                    description = "Check back later for new products.",
                )
            }
        }
    }
}

@Composable
private fun ProductsGrid(
    products: List<ProductItemUi>,
    onProductClick: (String) -> Unit,
    paddingValues: PaddingValues,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = paddingValues.calculateTopPadding() + 16.dp,
            bottom = paddingValues.calculateBottomPadding() + 16.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductCard(
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                onClick = { onProductClick(product.id) },
            )
        }
    }
}

private val placeholderProducts = listOf(
    ProductItemUi(id = "1", name = "Wireless Headphones", price = "$89.99", imageUrl = "https://picsum.photos/seed/headphones/400/400"),
    ProductItemUi(id = "2", name = "Smart Watch", price = "$199.99", imageUrl = "https://picsum.photos/seed/watch/400/400"),
    ProductItemUi(id = "3", name = "Laptop Stand", price = "$45.00", imageUrl = "https://picsum.photos/seed/stand/400/400"),
    ProductItemUi(id = "4", name = "USB-C Hub", price = "$29.99", imageUrl = "https://picsum.photos/seed/hub/400/400"),
    ProductItemUi(id = "5", name = "Mechanical Keyboard", price = "$129.99", imageUrl = "https://picsum.photos/seed/keyboard/400/400"),
    ProductItemUi(id = "6", name = "Wireless Mouse", price = "$39.99", imageUrl = "https://picsum.photos/seed/mouse/400/400"),
    ProductItemUi(id = "7", name = "Phone Case", price = "$19.99", imageUrl = "https://picsum.photos/seed/case/400/400"),
    ProductItemUi(id = "8", name = "Portable Charger", price = "$34.99", imageUrl = "https://picsum.photos/seed/charger/400/400"),
    ProductItemUi(id = "9", name = "Bluetooth Speaker", price = "$59.99", imageUrl = "https://picsum.photos/seed/speaker/400/400"),
    ProductItemUi(id = "10", name = "Webcam HD", price = "$79.99", imageUrl = "https://picsum.photos/seed/webcam/400/400"),
)

@Preview(showBackground = true)
@Composable
private fun AllProductListScreenPreview() {
    YVStoreTheme {
        AllProductListScreen(
            products = placeholderProducts,
            loadState = AllProductListLoadState.Loaded,
            onBackClick = {},
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AllProductListScreenLoadingPreview() {
    YVStoreTheme {
        AllProductListScreen(
            products = emptyList(),
            loadState = AllProductListLoadState.Loading,
            onBackClick = {},
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AllProductListScreenErrorPreview() {
    YVStoreTheme {
        AllProductListScreen(
            products = emptyList(),
            loadState = AllProductListLoadState.Error("Failed to load products. Please check your internet connection."),
            onBackClick = {},
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AllProductListScreenEmptyPreview() {
    YVStoreTheme {
        AllProductListScreen(
            products = emptyList(),
            loadState = AllProductListLoadState.Loaded,
            onBackClick = {},
            onProductClick = {},
        )
    }
}
