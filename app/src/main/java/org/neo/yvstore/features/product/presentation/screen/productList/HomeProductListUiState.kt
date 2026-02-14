package org.neo.yvstore.features.product.presentation.screen.productList

import org.neo.yvstore.features.product.presentation.model.ProductItemUi

data class HomeProductListUiState(
    val products: List<ProductItemUi> = emptyList(),
    val loadState: HomeProductListLoadState = HomeProductListLoadState.Loading,
    val hasCartItems: Boolean = false,
)

sealed class HomeProductListLoadState {
    data object Loading : HomeProductListLoadState()
    data object Loaded : HomeProductListLoadState()
    data class Error(val message: String) : HomeProductListLoadState()
}
