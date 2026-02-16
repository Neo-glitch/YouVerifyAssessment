package org.neo.yvstore.features.product.presentation.screen.allProductList

sealed class AllProductListUiEvent {
    data class Error(val message: String) : AllProductListUiEvent()
}
