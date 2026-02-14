package org.neo.yvstore.features.product.presentation.screen.searchProductList

import org.neo.yvstore.features.product.presentation.model.ProductItemUi


data class SearchProductListUiState(
    val query: String = "",
    val loadState: SearchProductListLoadState = SearchProductListLoadState.Idle,
)


sealed class SearchProductListLoadState {
    data object Idle : SearchProductListLoadState()

    data object Loading : SearchProductListLoadState()
    data class Loaded(val products: List<ProductItemUi>) : SearchProductListLoadState()
    data class Empty(val query: String) : SearchProductListLoadState()
    data class Error(val message: String) : SearchProductListLoadState()
}