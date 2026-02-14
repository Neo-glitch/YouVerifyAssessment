package org.neo.yvstore.features.product.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository

/**
 * Use case for retrieving a single product from local cache by ID.
 *
 * @property repository The product repository for accessing product data
 */
class GetProductUseCase(
    private val repository: ProductRepository
) {
    /**
     * Retrieves a product by its ID from the local database.
     *
     * @param id The product ID to retrieve
     * @return Resource containing the product or an error
     */
    suspend operator fun invoke(id: String): Resource<Product> =
        repository.getProduct(id)
}
