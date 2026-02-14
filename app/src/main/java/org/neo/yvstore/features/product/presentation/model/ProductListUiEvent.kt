package org.neo.yvstore.features.product.presentation.model

sealed class ProductListUiEvent {
    data class ShowToast(val message: String) : ProductListUiEvent()
}
