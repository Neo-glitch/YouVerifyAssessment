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
}
