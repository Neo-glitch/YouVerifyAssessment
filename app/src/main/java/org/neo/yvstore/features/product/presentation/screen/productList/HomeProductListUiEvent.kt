package org.neo.yvstore.features.product.presentation.screen.productList

sealed class HomeProductListUiEvent {
    data class Error(val message: String) : HomeProductListUiEvent()
}
