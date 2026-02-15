package org.neo.yvstore.features.order.presentation.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.features.address.domain.usecase.GetAddressByIdUseCase
import org.neo.yvstore.features.address.presentation.model.toAddressUi
import org.neo.yvstore.features.cart.domain.usecase.DeleteAllCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.GetCartItemsUseCase
import org.neo.yvstore.features.cart.presentation.model.toCartItemUi
import org.neo.yvstore.features.order.domain.model.OrderLineItem
import org.neo.yvstore.features.order.domain.usecase.PlaceOrderUseCase

class CheckoutViewModel(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val getAddressByIdUseCase: GetAddressByIdUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val deleteAllCartItemsUseCase: DeleteAllCartItemsUseCase,
    private val addressId: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CheckoutUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadCartItems()
        loadAddress()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            getCartItemsUseCase().onSuccess { cartItems ->
                _uiState.update {
                    it.copy(
                        cartItems = cartItems.map { item -> item.toCartItemUi() },
                        loadState = if (it.address != null) CheckoutLoadState.Loaded
                        else it.loadState,
                    )
                }
            }.onError { message ->
                _uiState.update {
                    it.copy(loadState = CheckoutLoadState.Error(message))
                }
            }
        }
    }

    private fun loadAddress() {
        viewModelScope.launch {
            val result = getAddressByIdUseCase(addressId)
            result.onSuccess { address ->
                _uiState.update {
                    it.copy(
                        address = address.toAddressUi(),
                        loadState = CheckoutLoadState.Loaded,
                    )
                }
            }.onError { message ->
                _uiState.update {
                    it.copy(loadState = CheckoutLoadState.Error(message))
                }
            }
        }
    }

    fun placeOrder() {
        val state = _uiState.value
        val address = state.address ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(placeOrderState = PlaceOrderState.Placing) }

            val result = placeOrderUseCase(
                totalAmount = state.total,
                shippingAddress = address.formattedAddress,
                items = state.cartItems.map { cartItem ->
                    OrderLineItem(
                        productId = cartItem.productId,
                        productName = cartItem.name,
                        unitPrice = cartItem.price,
                        quantity = cartItem.quantity
                    )
                }
            )

            result.onSuccess {
                deleteAllCartItemsUseCase()
                _uiEvent.send(CheckoutUiEvent.OrderPlaced)
            }.onError { message ->
                _uiState.update {
                    it.copy(placeOrderState = PlaceOrderState.Error(message))
                }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(placeOrderState = PlaceOrderState.Idle) }
    }
}
