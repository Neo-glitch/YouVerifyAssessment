package org.neo.yvstore.features.cart.presentation.screen.cartList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.features.cart.domain.usecase.DeleteAllCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.UpdateCartItemQuantityUseCase
import org.neo.yvstore.features.cart.presentation.model.toCartItemUi

class CartScreenViewModel(
    private val observeCartItemsUseCase: ObserveCartItemsUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val deleteAllCartItemsUseCase: DeleteAllCartItemsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState: StateFlow<CartScreenUiState> = _uiState.asStateFlow()

    init {
        observeCartItems()
    }

    private fun observeCartItems() {
        viewModelScope.launch {
            observeCartItemsUseCase().collect { resource ->
                resource.onSuccess { cartItems ->
                    _uiState.update {
                        it.copy(
                            cartItems = cartItems.map { item -> item.toCartItemUi() },
                            loadState = CartScreenLoadState.Loaded,
                        )
                    }
                }.onError { message ->
                    _uiState.update {
                        it.copy(loadState = CartScreenLoadState.Error(message))
                    }
                }
            }
        }
    }

    fun onIncrementQuantity(itemId: Long) {
        viewModelScope.launch {
            val currentItem = _uiState.value.cartItems.find { it.id == itemId } ?: return@launch
            val newQuantity = currentItem.quantity + 1
            updateCartItemQuantityUseCase(itemId, newQuantity)
        }
    }

    fun onDecrementQuantity(itemId: Long) {
        viewModelScope.launch {
            val currentItem = _uiState.value.cartItems.find { it.id == itemId } ?: return@launch
            val currentQuantity = currentItem.quantity
            if (currentQuantity > 1) {
                updateCartItemQuantityUseCase(itemId, currentQuantity - 1)
            }
        }
    }

    fun onRemoveItem(itemId: Long) {
        viewModelScope.launch {
            deleteCartItemUseCase(itemId)
        }
    }

    fun onShowClearCartDialog() {
        _uiState.update { it.copy(showClearCartDialog = true) }
    }

    fun onDismissClearCartDialog() {
        _uiState.update { it.copy(showClearCartDialog = false) }
    }

    fun onConfirmClearCart() {
        viewModelScope.launch {
            deleteAllCartItemsUseCase()
            _uiState.update { it.copy(showClearCartDialog = false) }
        }
    }
}
