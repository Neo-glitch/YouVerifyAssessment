package org.neo.yvstore.features.product.presentation.screen.allProductList

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
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.ObserveProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.features.product.presentation.model.ProductItemUi
import org.neo.yvstore.features.product.presentation.screen.productList.HomeProductListLoadState

class AllProductListViewModel(
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllProductListUiState())
    val uiState: StateFlow<AllProductListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AllProductListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        observeProducts()
        viewModelScope.launch {
            refreshProducts()
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            observeProductsUseCase(count = null).collect { resource ->
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

    fun onRefresh() {
        if (_uiState.value.loadState is AllProductListLoadState.Loading) return
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
        val products = observeProductsUseCase(count = null).first()
        products.onSuccess { items ->
            val loadState = if (items.isEmpty()) {
                AllProductListLoadState.Empty
            } else {
                AllProductListLoadState.Loaded
            }
            _uiState.update { it.copy(loadState = loadState) }
        }
    }

    private suspend fun handleRefreshError(message: String) {
        if (_uiState.value.products.isEmpty()) {
            _uiState.update { it.copy(loadState = AllProductListLoadState.Error(message)) }
        } else {
            _uiEvent.send(AllProductListUiEvent.Error(message))
        }
    }

    private fun Product.toProductItemUi() = ProductItemUi(
        id = id,
        name = name,
        price = price.formatAsPrice(),
        imageUrl = imageUrl,
    )
}
