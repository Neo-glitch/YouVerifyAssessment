package org.neo.yvstore.features.product.presentation.screen.productDetails

sealed class ProductDetailsUiEvent {
    data class Error(val message: String) : ProductDetailsUiEvent()
}
