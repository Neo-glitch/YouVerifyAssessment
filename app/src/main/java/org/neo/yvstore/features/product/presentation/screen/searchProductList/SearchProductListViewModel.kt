package org.neo.yvstore.features.product.presentation.screen.searchProductList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.SearchProductsUseCase
import org.neo.yvstore.features.product.presentation.model.ProductItemUi
import org.neo.yvstore.features.product.presentation.model.SearchProductListLoadState
import org.neo.yvstore.features.product.presentation.model.SearchProductListUiState

/**
 * ViewModel for the search product list screen.
 * Manages search query state and remote product search with debouncing.
 *
 * @property searchProductsUseCase Use case for searching products remotely
 */
class SearchProductListViewModel(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchProductListUiState())
    val uiState: StateFlow<SearchProductListUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    companion object {
        private const val DEBOUNCE_DELAY_MS = 500L
    }

    /**
     * Updates the search query and triggers a debounced remote search.
     * Cancels previous search job and starts a new one with the updated query.
     *
     * @param query The new search query
     */
    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }

        // Cancel previous search
        searchJob?.cancel()

        if (query.isEmpty()) {
            _uiState.update { it.copy(loadState = SearchProductListLoadState.Idle) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY_MS)
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(loadState = SearchProductListLoadState.Loading) }

        val result = searchProductsUseCase(query)
        result.onSuccess { products ->
            _uiState.update {
                if (products.isEmpty()) {
                    it.copy(loadState = SearchProductListLoadState.Empty(query))
                } else {
                    it.copy(
                        loadState = SearchProductListLoadState.Loaded(
                            products = products.map { product -> product.toProductItemUi() }
                        )
                    )
                }
            }
        }.onError { message ->
            _uiState.update {
                it.copy(loadState = SearchProductListLoadState.Error(message))
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
