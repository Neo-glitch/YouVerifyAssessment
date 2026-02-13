package org.neo.yvstore.features.product.presentation.screen.productDetails

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.neo.yvstore.features.product.presentation.model.ProductDetailsUi

data class ProductDetailsUiState(
    val product: ProductDetailsUi? = null,
    val quantity: Int = 0,
) {
    val totalPrice: Double
        get() = (product?.price ?: 0.0) * quantity

    val formattedTotalPrice: String
        get() = "$%.2f".format(totalPrice)
}

class ProductDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductDetailsUiState(
            product = placeholderProduct
        )
    )
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun onAddToCart() {
        _uiState.update { currentState ->
            currentState.copy(quantity = 1)
        }
    }

    fun onIncrementQuantity() {
        _uiState.update { currentState ->
            currentState.copy(quantity = currentState.quantity + 1)
        }
    }

    fun onDecrementQuantity() {
        _uiState.update { currentState ->
            val newQuantity = (currentState.quantity - 1).coerceAtLeast(0)
            currentState.copy(quantity = newQuantity)
        }
    }

    companion object {
        private val placeholderProduct = ProductDetailsUi(
            id = "1",
            name = "Wireless Headphones",
            description = "Premium sound quality with active noise cancellation. Experience music like never before with deep bass and crystal clear highs.",
            price = 89.99,
            imageUrl = "https://picsum.photos/seed/headphones/400/400",
            rating = 4.5,
            reviewCount = 128,
            details = "These wireless headphones feature Bluetooth 5.0 connectivity, 30-hour battery life, and comfortable over-ear design. Perfect for music lovers, gamers, and professionals who demand high-quality audio. Includes a carrying case and audio cable for wired connection."
        )
    }
}
