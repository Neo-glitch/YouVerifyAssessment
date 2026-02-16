package org.neo.yvstore.features.product.presentation.screen.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.core.common.util.formatAsPrice
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemCountUseCase
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.ObserveProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.features.product.presentation.model.ProductItemUi

class HomeProductListViewModel(
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase,
    private val observeCartItemCountUseCase: ObserveCartItemCountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeProductListUiState())
    val uiState: StateFlow<HomeProductListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeProductListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    companion object {
        private const val PRODUCT_COUNT = 10
    }

    init {
        observeProducts()
        observeCartItemCount()
        viewModelScope.launch {
            refreshProducts()
        }
    }

    private fun observeCartItemCount() {
        viewModelScope.launch {
            observeCartItemCountUseCase().collect { resource ->
                resource.onSuccess { count ->
                    _uiState.update {
                        it.copy(cartItemCount = count)
                    }
                }
            }
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            observeProductsUseCase(count = PRODUCT_COUNT).collect { resource ->
                resource.onSuccess { products ->
                    _uiState.update {
                        it.copy(
                            products = products.map { product -> product.toProductItemUi() },
                        )
                    }
                }.onError { error ->
                    _uiState.update {
                        it.copy(loadState = HomeProductListLoadState.Error(error))
                    }
                }
            }
        }
    }

    fun onRefresh() {
        if (_uiState.value.loadState is HomeProductListLoadState.Loading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            refreshProductsUseCase()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun refreshProducts() {
        refreshProductsUseCase()
            .onSuccess { handleRefreshSuccess() }
            .onError { message -> handleRefreshError(message) }
    }

    private suspend fun handleRefreshSuccess() {
        val products = observeProductsUseCase(PRODUCT_COUNT).first()
        products.onSuccess { items ->
            val loadState = if (items.isEmpty()) {
                HomeProductListLoadState.Empty
            } else {
                HomeProductListLoadState.Loaded
            }
            _uiState.update { it.copy(loadState = loadState) }
        }
    }

    private suspend fun handleRefreshError(message: String) {
        if (_uiState.value.products.isEmpty()) {
            _uiState.update { it.copy(loadState = HomeProductListLoadState.Error(message)) }
        } else {
            _uiEvent.send(HomeProductListUiEvent.Error(message))
        }
    }

    private fun Product.toProductItemUi() = ProductItemUi(
        id = id,
        name = name,
        price = price.formatAsPrice(),
        imageUrl = imageUrl,
    )
}
