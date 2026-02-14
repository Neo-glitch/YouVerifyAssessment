package org.neo.yvstore.features.product.presentation.model

sealed class ProductListLoadState {
    data object Loading : ProductListLoadState()
    data object Loaded : ProductListLoadState()
    data class Error(val message: String) : ProductListLoadState()
}
