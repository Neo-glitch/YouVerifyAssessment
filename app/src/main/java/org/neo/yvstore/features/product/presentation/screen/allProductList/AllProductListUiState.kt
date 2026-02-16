package org.neo.yvstore.features.product.presentation.screen.allProductList

import org.neo.yvstore.features.product.presentation.model.ProductItemUi

data class AllProductListUiState(
    val products: List<ProductItemUi> = emptyList(),
    val loadState: AllProductListLoadState = AllProductListLoadState.Loading,
    val hasCartItems: Boolean = false,
    val isRefreshing: Boolean = false,
)

sealed class AllProductListLoadState {
    data object Loading : AllProductListLoadState()
    data object Loaded : AllProductListLoadState()
    data object Empty : AllProductListLoadState()
    data class Error(val message: String) : AllProductListLoadState()
}
