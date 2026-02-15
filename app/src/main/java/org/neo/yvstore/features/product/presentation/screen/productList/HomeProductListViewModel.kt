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
        viewModelScope.launch {
            loadCachedProducts()
            observeProducts()
            observeCartItemCount()
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

    private suspend fun loadCachedProducts() {
        val initialResult = observeProductsUseCase(PRODUCT_COUNT).first()
        initialResult.onSuccess { products ->
            if (products.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        products = products.map { product -> product.toProductItemUi() },
                        loadState = HomeProductListLoadState.Loaded,
                    )
                }
            }
        }.onError { error ->
            _uiState.update {
                it.copy(loadState = HomeProductListLoadState.Error(error))
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
                }
            }
        }
    }

    private suspend fun refreshProducts() {
        val result = refreshProductsUseCase()
        result.onError { message ->
            val currentState = _uiState.value
            if (currentState.products.isEmpty() && currentState.loadState !is HomeProductListLoadState.Error) {
                _uiState.update {
                    it.copy(loadState = HomeProductListLoadState.Error(message))
                }
            } else if (currentState.products.isNotEmpty()) {
                _uiEvent.send(HomeProductListUiEvent.ShowToast(message))
            }
        }
    }

    private fun Product.toProductItemUi() = ProductItemUi(
        id = id,
        name = name,
        price = price.formatAsPrice(),
        imageUrl = imageUrl,
    )
}
