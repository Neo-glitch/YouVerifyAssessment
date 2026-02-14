package org.neo.yvstore.features.product.presentation.model

/**
 * UI state for the search product list screen.
 *
 * @property query Current search input text
 * @property loadState The current load state of the search
 */
data class SearchProductListUiState(
    val query: String = "",
    val loadState: SearchProductListLoadState = SearchProductListLoadState.Idle,
)
