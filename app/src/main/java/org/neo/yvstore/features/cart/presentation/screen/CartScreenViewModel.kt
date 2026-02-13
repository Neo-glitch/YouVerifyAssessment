package org.neo.yvstore.features.cart.presentation.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.neo.yvstore.features.cart.presentation.model.CartItemUi

data class CartScreenUiState(
    val cartItems: List<CartItemUi> = emptyList(),
    val deliveryFee: Double = 5.0,
    val showClearCartDialog: Boolean = false,
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

    val isEmpty: Boolean
        get() = cartItems.isEmpty()
}

class CartScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        CartScreenUiState(
            cartItems = placeholderCartItems
        )
    )
    val uiState: StateFlow<CartScreenUiState> = _uiState.asStateFlow()

    fun onIncrementQuantity(itemId: String) {
        _uiState.update { currentState ->
            val updatedItems = currentState.cartItems.map { item ->
                if (item.id == itemId) {
                    item.copy(quantity = item.quantity + 1)
                } else {
                    item
                }
            }
            currentState.copy(cartItems = updatedItems)
        }
    }

    fun onDecrementQuantity(itemId: String) {
        _uiState.update { currentState ->
            val updatedItems = currentState.cartItems.map { item ->
                if (item.id == itemId) {
                    item.copy(quantity = (item.quantity - 1).coerceAtLeast(1))
                } else {
                    item
                }
            }
            currentState.copy(cartItems = updatedItems)
        }
    }

    fun onRemoveItem(itemId: String) {
        _uiState.update { currentState ->
            val updatedItems = currentState.cartItems.filter { it.id != itemId }
            currentState.copy(cartItems = updatedItems)
        }
    }

    fun onShowClearCartDialog() {
        _uiState.update { it.copy(showClearCartDialog = true) }
    }

    fun onDismissClearCartDialog() {
        _uiState.update { it.copy(showClearCartDialog = false) }
    }

    fun onConfirmClearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    companion object {
        private val placeholderCartItems = listOf(
            CartItemUi(
                id = "1",
                name = "Xbox series X",
                variantLabel = "1 TB",
                price = 570.0,
                imageUrl = "https://picsum.photos/seed/xbox/400/400",
                quantity = 1,
            ),
            CartItemUi(
                id = "2",
                name = "PlayStation 5",
                variantLabel = "Standard Edition",
                price = 499.99,
                imageUrl = "https://picsum.photos/seed/ps5/400/400",
                quantity = 1,
            ),
            CartItemUi(
                id = "3",
                name = "Nintendo Switch OLED",
                variantLabel = "White",
                price = 349.99,
                imageUrl = "https://picsum.photos/seed/switch/400/400",
                quantity = 2,
            ),
        )
    }
}
