package org.neo.yvstore.features.product.presentation.model

/**
 * Represents the load state of the search product list screen.
 */
sealed class SearchProductListLoadState {
    /**
     * Initial state before any search has been performed.
     * Shows a prompt to start searching.
     */
    data object Idle : SearchProductListLoadState()

    /**
     * A search is in progress.
     */
    data object Loading : SearchProductListLoadState()

    /**
     * Search completed with results.
     *
     * @property products List of products matching the search query
     */
    data class Loaded(val products: List<ProductItemUi>) : SearchProductListLoadState()

    /**
     * Search completed with zero results for the given non-empty query.
     *
     * @property query The search query that returned no results
     */
    data class Empty(val query: String) : SearchProductListLoadState()

    /**
     * Search failed with an error.
     *
     * @property message The error message
     */
    data class Error(val message: String) : SearchProductListLoadState()
}
