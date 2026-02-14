package org.neo.yvstore.features.cart.presentation.screen

import org.neo.yvstore.features.cart.presentation.model.CartItemUi

data class CartScreenUiState(
    val cartItems: List<CartItemUi> = emptyList(),
    val deliveryFee: Double = 5.0,
    val showClearCartDialog: Boolean = false,
    val loadState: CartScreenLoadState = CartScreenLoadState.Loading,
) {
    val subtotal: Double
        get() = cartItems.sumOf { it.itemTotal }

    val total: Double
        get() = subtotal + deliveryFee

    val formattedSubtotal: String
        get() = "$%.2f".format(subtotal)

    val formattedDeliveryFee: String
        get() = "$%.2f".format(deliveryFee)

    val formattedTotal: String
        get() = "$%.2f".format(total)
}

sealed class CartScreenLoadState {
    data object Loading : CartScreenLoadState()
    data object Loaded : CartScreenLoadState()
    data class Error(val message: String) : CartScreenLoadState()
}
