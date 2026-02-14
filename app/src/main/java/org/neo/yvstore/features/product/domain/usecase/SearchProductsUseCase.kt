package org.neo.yvstore.features.product.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository

/**
 * Use case for performing one-shot product searches from remote datasource.
 * Retrieves products from Firestore with optional search query.
 *
 * @property repository The product repository for accessing product data
 */
class SearchProductsUseCase(
    private val repository: ProductRepository
) {
    /**
     * Performs a one-shot remote search for products filtered by a search query.
     * If query is empty, returns all remote products.
     *
     * @param query Search query to filter products by name (empty for all products)
     * @return Resource.Success with product list or Resource.Error with failure message
     */
    suspend operator fun invoke(query: String = ""): Resource<List<Product>> =
        repository.searchProducts(query)
}
