package org.neo.yvstore.features.order.presentation.screen.checkout

import org.neo.yvstore.core.common.util.formatAsPrice
import org.neo.yvstore.features.address.presentation.model.AddressUi
import org.neo.yvstore.features.cart.presentation.model.CartItemUi

data class CheckoutUiState(
    val cartItems: List<CartItemUi> = emptyList(),
    val address: AddressUi? = null,
    val deliveryFee: Double = 5.0,
    val loadState: CheckoutLoadState = CheckoutLoadState.Loading,
    val placeOrderState: PlaceOrderState = PlaceOrderState.Idle,
) {
    val subtotal: Double
        get() = cartItems.sumOf { it.itemTotal }

    val total: Double
        get() = subtotal + deliveryFee

    val formattedSubtotal: String
        get() = subtotal.formatAsPrice()

    val formattedDeliveryFee: String
        get() = deliveryFee.formatAsPrice()

    val formattedTotal: String
        get() = total.formatAsPrice()
}

sealed class CheckoutLoadState {
    data object Loading : CheckoutLoadState()
    data object Loaded : CheckoutLoadState()
    data class Error(val message: String) : CheckoutLoadState()
}

sealed class PlaceOrderState {
    data object Idle : PlaceOrderState()
    data object Placing : PlaceOrderState()
    data class Error(val message: String) : PlaceOrderState()
}

sealed class CheckoutUiEvent {
    data object OrderPlaced : CheckoutUiEvent()
}
