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

/**
 * ViewModel for the All Products List screen.
 * Manages UI state for displaying all products with 3-step initialization:
 * 1. Load cached products immediately
 * 2. Observe products from Room for live updates
 * 3. Refresh products from remote datasource
 */
class AllProductListViewModel(
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllProductListUiState())
    val uiState: StateFlow<AllProductListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AllProductListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadCachedProducts()
            observeProducts()
            refreshProducts()
        }
    }

    private suspend fun loadCachedProducts() {
        val initialResult = observeProductsUseCase(count = null).first()
        initialResult.onSuccess { products ->
            if (products.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        products = products.map { product -> product.toProductItemUi() },
                        loadState = AllProductListLoadState.Loaded,
                    )
                }
            }
        }.onError { error ->
            _uiState.update {
                it.copy(loadState = AllProductListLoadState.Error(error))
            }
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
        val result = refreshProductsUseCase()
        result.onSuccess {
            val currentState = _uiState.value
            _uiState.update {
                it.copy(
                    loadState = if (currentState.products.isEmpty()) {
                        AllProductListLoadState.Empty
                    } else {
                        AllProductListLoadState.Loaded
                    }
                )
            }
        }.onError { message ->
            val currentState = _uiState.value
            if (currentState.products.isEmpty() && currentState.loadState !is AllProductListLoadState.Error) {
                _uiState.update {
                    it.copy(loadState = AllProductListLoadState.Error(message))
                }
            } else if (currentState.products.isNotEmpty()) {
                _uiEvent.send(AllProductListUiEvent.Error(message))
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
