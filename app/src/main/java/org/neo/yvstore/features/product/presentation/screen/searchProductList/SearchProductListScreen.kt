package org.neo.yvstore.features.product.presentation.screen.searchProductList

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.grid.NonlazyGrid
import org.neo.yvstore.core.ui.component.input.YVStoreSearchInput
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.features.product.presentation.model.ProductItemUi
import org.neo.yvstore.features.product.presentation.screen.productList.components.ProductCard

private const val SEARCH_DEBOUNCE_MS = 500L

@Composable
fun SearchProductListScreen(
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: SearchProductListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchProductListScreen(
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChanged,
        onSearch = viewModel::onSearch,
        onBackClick = onBackClick,
        onProductClick = onProductClick,
    )
}

@Composable
private fun SearchProductListScreen(
    uiState: SearchProductListUiState,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit,
) {
    YVStoreScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchHeader(
                query = uiState.query,
                onQueryChanged = onQueryChanged,
                onSearch = onSearch,
                onBackClick = onBackClick,
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val loadState = uiState.loadState) {
                SearchProductListLoadState.Idle -> {
                    IdleContent(modifier = Modifier.weight(1f))
                }
                SearchProductListLoadState.Loading -> {
                    LoadingContent(modifier = Modifier.weight(1f))
                }
                is SearchProductListLoadState.Loaded -> {
                    ProductListContent(
                        products = loadState.products,
                        onProductClick = onProductClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                is SearchProductListLoadState.Empty -> {
                    EmptySearchContent(
                        query = loadState.query,
                        modifier = Modifier.weight(1f)
                    )
                }
                is SearchProductListLoadState.Error -> {
                    ErrorContent(
                        message = loadState.message,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackClick,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = "Back",
                tint = YVStoreTheme.colors.navigationColors.navigationIcon,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        YVStoreSearchInput(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = "Search products...",
            autoFocus = true,
            debounceDelay = SEARCH_DEBOUNCE_MS,
            onSearch = onSearch,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun IdleContent(modifier: Modifier = Modifier) {
    CenteredContent(modifier = modifier) {
        Text(
            text = "Start typing to search products",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    CenteredContent(modifier = modifier) {
        YVStoreCircleProgressIndicator(size = 48.dp)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    CenteredContent(modifier = modifier) {
        YVStoreEmptyErrorStateView(
            image = android.R.drawable.ic_dialog_alert,
            title = "Search Error",
            description = message,
        )
    }
}

@Composable
private fun EmptySearchContent(
    query: String,
    modifier: Modifier = Modifier,
) {
    CenteredContent(modifier = modifier) {
        YVStoreEmptyErrorStateView(
            image = R.drawable.ic_search,
            title = "No results found",
            description = "We couldn't find any products matching \"$query\"",
        )
    }
}

@Composable
private fun ProductListContent(
    products: List<ProductItemUi>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "${products.size} product${if (products.size != 1) "s" else ""} found",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(16.dp))

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

private val placeholderProducts = listOf(
    ProductItemUi(id = "1", name = "Wireless Headphones", price = "$89.99", imageUrl = "https://picsum.photos/seed/headphones/400/400"),
    ProductItemUi(id = "2", name = "Smart Watch", price = "$199.99", imageUrl = "https://picsum.photos/seed/watch/400/400"),
    ProductItemUi(id = "3", name = "Laptop Stand", price = "$45.00", imageUrl = "https://picsum.photos/seed/stand/400/400"),
)

@Preview(showBackground = true)
@Composable
private fun SearchProductListScreenIdlePreview() {
    YVStoreTheme {
        SearchProductListScreen(
            uiState = SearchProductListUiState(),
            onQueryChanged = {},
            onSearch = {},
            onBackClick = {},
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchProductListScreenLoadingPreview() {
    YVStoreTheme {
        SearchProductListScreen(
            uiState = SearchProductListUiState(
                query = "headphones",
                loadState = SearchProductListLoadState.Loading,
            ),
            onQueryChanged = {},
            onSearch = {},
            onBackClick = {},
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchProductListScreenLoadedPreview() {
    YVStoreTheme {
        SearchProductListScreen(
            uiState = SearchProductListUiState(
                query = "headphones",
                loadState = SearchProductListLoadState.Loaded(placeholderProducts),
            ),
            onQueryChanged = {},
            onSearch = {},
            onBackClick = {},
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchProductListScreenEmptyPreview() {
    YVStoreTheme {
        SearchProductListScreen(
            uiState = SearchProductListUiState(
                query = "xyz",
                loadState = SearchProductListLoadState.Empty("xyz"),
            ),
            onQueryChanged = {},
            onSearch = {},
            onBackClick = {},
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchProductListScreenErrorPreview() {
    YVStoreTheme {
        SearchProductListScreen(
            uiState = SearchProductListUiState(
                query = "test",
                loadState = SearchProductListLoadState.Error("Unable to search products. Please try again."),
            ),
            onQueryChanged = {},
            onSearch = {},
            onBackClick = {},
            onProductClick = {},
        )
    }
}
