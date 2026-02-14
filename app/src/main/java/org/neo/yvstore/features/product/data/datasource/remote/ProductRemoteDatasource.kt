package org.neo.yvstore.features.product.data.datasource.remote

import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto

/**
 * Remote datasource interface for product operations using Firestore.
 * Methods throw exceptions on failure - repository layer handles error wrapping.
 */
interface ProductRemoteDatasource {
    /**
     * Retrieves all products from the Firestore "products" collection.
     *
     * @return List of ProductDto objects
     * @throws Exception on failure (network error, Firestore error)
     */
    suspend fun getProducts(): List<ProductDto>

    /**
     * Searches products from the Firestore "products" collection.
     * Fetches all products and filters client-side by name contains query (case-insensitive).
     * If query is empty, returns all products.
     *
     * @param query Search query to filter products by name (empty for all products)
     * @return List of ProductDto objects matching the query
     * @throws Exception on failure (network error, Firestore error)
     */
    suspend fun searchProducts(query: String = ""): List<ProductDto>
}
