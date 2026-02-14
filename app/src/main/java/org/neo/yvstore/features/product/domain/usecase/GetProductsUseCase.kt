package org.neo.yvstore.features.product.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository

/**
 * Use case for observing products from local cache.
 * Returns a reactive Flow that emits whenever the local products table changes.
 *
 * @property repository The product repository for accessing product data
 */
class GetProductsUseCase(
    private val repository: ProductRepository
) {
    /**
     * Returns a Flow of products wrapped in Resource, limited to the specified count.
     * Emits Resource.Success with product lists or Resource.Error on failure.
     *
     * @param count Maximum number of products to retrieve
     * @return Flow of Resource-wrapped product lists
     */
    operator fun invoke(count: Int): Flow<Resource<List<Product>>> = repository.getProducts(count)
}
