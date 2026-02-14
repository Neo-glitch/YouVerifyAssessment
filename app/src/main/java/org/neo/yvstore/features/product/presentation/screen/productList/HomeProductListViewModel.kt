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
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.GetProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.features.product.presentation.model.ProductItemUi
import org.neo.yvstore.features.product.presentation.model.ProductListLoadState
import org.neo.yvstore.features.product.presentation.model.ProductListUiEvent
import org.neo.yvstore.features.product.presentation.model.ProductListUiState

class HomeProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ProductListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    companion object Companion {
        private const val PRODUCT_COUNT = 20
    }

    init {
        viewModelScope.launch {
            loadCachedProducts()
            observeProducts()
            refreshProducts()
        }
    }

    private suspend fun loadCachedProducts() {
        val initialResult = getProductsUseCase(PRODUCT_COUNT).first()
        initialResult.onSuccess { products ->
            if (products.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        products = products.map { product -> product.toProductItemUi() },
                        loadState = ProductListLoadState.Loaded,
                    )
                }
            }
        }.onError { error ->
            _uiState.update {
                it.copy(loadState = ProductListLoadState.Error(error))
            }
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            getProductsUseCase(PRODUCT_COUNT).collect { resource ->
                resource.onSuccess { products ->
                    _uiState.update {
                        it.copy(
                            products = products.map { product -> product.toProductItemUi() },
                            loadState = ProductListLoadState.Loaded,
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
            if (currentState.products.isEmpty() && currentState.loadState !is ProductListLoadState.Error) {
                _uiState.update {
                    it.copy(loadState = ProductListLoadState.Error(message))
                }
            } else if (currentState.products.isNotEmpty()) {
                _uiEvent.send(ProductListUiEvent.ShowToast(message))
            }
        }
    }

    private fun Product.toProductItemUi() = ProductItemUi(
        id = id,
        name = name,
        price = "$%.2f".format(price),
        imageUrl = imageUrl,
    )
}
