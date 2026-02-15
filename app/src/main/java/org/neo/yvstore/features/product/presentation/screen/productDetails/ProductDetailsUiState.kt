package org.neo.yvstore.features.product.presentation.screen.productDetails

import org.neo.yvstore.core.common.util.formatAsPrice
import org.neo.yvstore.features.product.presentation.model.ProductDetailsUi

data class ProductDetailsUiState(
    val product: ProductDetailsUi? = null,
    val quantity: Int = 0,
    val cartItemId: Long? = null,
    val loadState: ProductDetailsLoadState = ProductDetailsLoadState.Loading,
) {
    val totalPrice: Double
        get() = (product?.price ?: 0.0) * quantity

    val formattedTotalPrice: String
        get() = totalPrice.formatAsPrice()
}

sealed class ProductDetailsLoadState {
    data object Loading : ProductDetailsLoadState()
    data object Loaded : ProductDetailsLoadState()
    data class Error(val message: String) : ProductDetailsLoadState()
}
