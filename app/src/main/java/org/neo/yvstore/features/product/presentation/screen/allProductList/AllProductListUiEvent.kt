package org.neo.yvstore.features.product.presentation.screen.allProductList

sealed class AllProductListUiEvent {
    data class ShowToast(val message: String) : AllProductListUiEvent()
}
