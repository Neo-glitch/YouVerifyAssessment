package org.neo.yvstore.features.product.presentation.screen.productList

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.image.YVStoreImage
import org.neo.yvstore.core.ui.component.button.YVStoreTextButton
import org.neo.yvstore.core.ui.component.grid.NonlazyGrid
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.ui.component.surface.YVStorePullToRefreshBox
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.util.ObserveAsEvents
import org.neo.yvstore.features.product.presentation.model.ProductItemUi
import org.neo.yvstore.features.product.presentation.screen.productList.components.CartIcon
import org.neo.yvstore.features.product.presentation.screen.productList.components.ProductItemCard
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
            is HomeProductListUiEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    HomeProductListScreen(
        products = uiState.products,
        loadState = uiState.loadState,
        cartItemCount = uiState.cartItemCount,
        isRefreshing = uiState.isRefreshing,
        promoTitle = "Clearance Sales",
        promoDiscountText = "Up to 50%",
        promoImageUrl = "https://picsum.photos/seed/promo/200/200",
        onNavigateToCart = onNavigateToCart,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToProductDetails = onNavigateToProductDetails,
        onViewAllClick = onViewAllClick,
        onRefresh = viewModel::onRefresh,
    )
}

@Composable
private fun HomeProductListScreen(
    products: List<ProductItemUi>,
    loadState: HomeProductListLoadState,
    cartItemCount: Int,
    isRefreshing: Boolean,
    promoTitle: String,
    promoDiscountText: String,
    promoImageUrl: String,
    onNavigateToCart: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onViewAllClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    YVStoreScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderSection(
                cartItemCount = cartItemCount,
                onCartClick = onNavigateToCart,
            )

            Spacer(modifier = Modifier.height(16.dp))
            SearchBarPlaceholder(
                onClick = onNavigateToSearch,
            )

            YVStorePullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.weight(1f),
            ) {
                HomeProductListContent(
                    loadState = loadState,
                    products = products,
                    promoTitle = promoTitle,
                    promoDiscountText = promoDiscountText,
                    promoImageUrl = promoImageUrl,
                    onNavigateToProductDetails = onNavigateToProductDetails,
                    onViewAllClick = onViewAllClick,
                )
            }
        }
    }
}

@Composable
private fun HomeProductListContent(
    loadState: HomeProductListLoadState,
    products: List<ProductItemUi>,
    promoTitle: String,
    promoDiscountText: String,
    promoImageUrl: String,
    onNavigateToProductDetails: (String) -> Unit,
    onViewAllClick: () -> Unit,
) {
    when (loadState) {
        HomeProductListLoadState.Loading -> {
            CenteredContent(modifier = Modifier.fillMaxSize()) {
                YVStoreCircleProgressIndicator(size = 48.dp)
            }
        }
        is HomeProductListLoadState.Error -> {
            CenteredContent(modifier = Modifier.fillMaxSize()) {
                YVStoreEmptyErrorStateView(
                    image = R.drawable.ic_error,
                    title = "Unable to Load Products",
                    description = loadState.message,
                )
            }
        }
        HomeProductListLoadState.Loaded -> {
            if (products.isEmpty()) {
                CenteredContent(modifier = Modifier.fillMaxSize()) {
                    YVStoreEmptyErrorStateView(
                        image = R.drawable.ic_empty_products,
                        title = "No Products Available",
                        description = "Check back later for new products.",
                    )
                }
            } else {
                ProductListContent(
                    products = products,
                    promoTitle = promoTitle,
                    promoDiscountText = promoDiscountText,
                    promoImageUrl = promoImageUrl,
                    onNavigateToProductDetails = onNavigateToProductDetails,
                    onViewAllClick = onViewAllClick,
                )
            }
        }
    }
}

@Composable
private fun ProductListContent(
    products: List<ProductItemUi>,
    promoTitle: String,
    promoDiscountText: String,
    promoImageUrl: String,
    onNavigateToProductDetails: (String) -> Unit,
    onViewAllClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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

@Composable
private fun CenteredContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Composable
private fun HeaderSection(
    cartItemCount: Int,
    onCartClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )

        CartIcon(
            cartItemCount = cartItemCount,
            onClick = onCartClick,
        )
    }
}

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
                    style = MaterialTheme.typography.bodyLarge.copy(
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
            YVStoreImage(
                imageUrl = imageUrl,
                contentDescription = "Promotional product",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit,
            )
        }
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
            style = MaterialTheme.typography.titleMedium.copy(
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
        ProductItemCard(
            name = product.name,
            price = product.price,
            imageUrl = product.imageUrl,
            onClick = { onProductClick(product.id) },
        )
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
private fun HomeProductListScreenPreview() {
    PreviewContent(products = placeholderProducts)
}

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenWithCartItemsPreview() {
    PreviewContent(products = placeholderProducts, cartItemCount = 3)
}

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenWithManyCartItemsPreview() {
    PreviewContent(products = placeholderProducts, cartItemCount = 15)
}

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenLoadingPreview() {
    PreviewContent(products = emptyList(), loadState = HomeProductListLoadState.Loading)
}

@Preview(showBackground = true)
@Composable
private fun HomeProductListScreenErrorPreview() {
    PreviewContent(
        products = emptyList(),
        loadState = HomeProductListLoadState.Error("Failed to load products. Please check your internet connection."),
    )
}

@Composable
private fun PreviewContent(
    products: List<ProductItemUi> = placeholderProducts,
    loadState: HomeProductListLoadState = HomeProductListLoadState.Loaded,
    cartItemCount: Int = 0,
    isRefreshing: Boolean = false,
) {
    YVStoreTheme {
        HomeProductListScreen(
            products = products,
            loadState = loadState,
            cartItemCount = cartItemCount,
            isRefreshing = isRefreshing,
            promoTitle = "Clearance Sales",
            promoDiscountText = "Up to 50%",
            promoImageUrl = "https://picsum.photos/seed/promo/200/200",
            onNavigateToCart = {},
            onNavigateToSearch = {},
            onNavigateToProductDetails = {},
            onViewAllClick = {},
            onRefresh = {},
        )
    }
}
