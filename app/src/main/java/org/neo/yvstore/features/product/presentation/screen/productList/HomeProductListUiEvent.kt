package org.neo.yvstore.features.product.presentation.screen.productList

sealed class HomeProductListUiEvent {
    data class ShowToast(val message: String) : HomeProductListUiEvent()
}
