package org.neo.yvstore.features.product.presentation.screen.productDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.features.cart.domain.usecase.AddCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemByProductIdUseCase
import org.neo.yvstore.features.cart.domain.usecase.UpdateCartItemQuantityUseCase
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.GetProductUseCase
import org.neo.yvstore.features.product.presentation.model.ProductDetailsUi

class ProductDetailsViewModel(
    private val getProductUseCase: GetProductUseCase,
    private val observeCartItemByProductIdUseCase: ObserveCartItemByProductIdUseCase,
    private val addCartItemUseCase: AddCartItemUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
) : ViewModel() {

    private var productId: String = ""
    private var hasInitialized: Boolean = false

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ProductDetailsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(productId: String) {
        if (hasInitialized) return
        hasInitialized = true
        this.productId = productId
        viewModelScope.launch {
            loadProduct()
            observeCartItem()
        }
    }

    private suspend fun loadProduct() {
        val result = getProductUseCase(productId)
        result.onSuccess { product ->
            _uiState.update {
                it.copy(
                    product = product.toProductDetailsUi(),
                    loadState = ProductDetailsLoadState.Loaded,
                )
            }
        }.onError { message ->
            _uiState.update {
                it.copy(loadState = ProductDetailsLoadState.Error(message))
            }
        }
    }

    private fun observeCartItem() {
        viewModelScope.launch {
            observeCartItemByProductIdUseCase(productId).collect { resource ->
                resource.onSuccess { cartItem ->
                    _uiState.update {
                        it.copy(
                            quantity = cartItem?.quantity ?: 0,
                            cartItemId = cartItem?.id,
                        )
                    }
                }
            }
        }
    }

    fun onAddToCart() {
        viewModelScope.launch {
            val currentProduct = _uiState.value.product ?: return@launch
            val cartItem = CartItemEntity(
                id = 0,
                productId = currentProduct.id,
                productName = currentProduct.name,
                productImageUrl = currentProduct.imageUrl,
                unitPrice = currentProduct.price,
                quantity = 1,
            )
            val result = addCartItemUseCase(cartItem)
            result.onError { message ->
                _uiEvent.send(ProductDetailsUiEvent.Error(message))
            }
        }
    }

    fun onIncrementQuantity() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val cartItemId = currentState.cartItemId ?: return@launch
            val newQuantity = currentState.quantity + 1
            val result = updateCartItemQuantityUseCase(cartItemId, newQuantity)
            result.onError { message ->
                _uiEvent.send(ProductDetailsUiEvent.Error(message))
            }
        }
    }

    fun onDecrementQuantity() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val cartItemId = currentState.cartItemId ?: return@launch
            val currentQuantity = currentState.quantity

            if (currentQuantity > 1) {
                val newQuantity = currentQuantity - 1
                val result = updateCartItemQuantityUseCase(cartItemId, newQuantity)
                result.onError { message ->
                    _uiEvent.send(ProductDetailsUiEvent.Error(message))
                }
            } else if (currentQuantity == 1) {
                val result = deleteCartItemUseCase(cartItemId)
                result.onError { message ->
                    _uiEvent.send(ProductDetailsUiEvent.Error(message))
                }
            }
        }
    }

    private fun Product.toProductDetailsUi() = ProductDetailsUi(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        details = description,
    )
}
