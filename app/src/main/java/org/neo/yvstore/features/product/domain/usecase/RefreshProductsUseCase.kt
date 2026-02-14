package org.neo.yvstore.features.product.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.repository.ProductRepository

/**
 * Use case for refreshing products from remote datasource.
 * Fetches products from Firestore and caches them locally in Room.
 *
 * @property repository The product repository for performing refresh operations
 */
class RefreshProductsUseCase(
    private val repository: ProductRepository
) {
    /**
     * Triggers a remote fetch and updates the local cache.
     *
     * @return Resource.Success with Unit on successful refresh, Resource.Error with failure message
     */
    suspend operator fun invoke(): Resource<Unit> = repository.refreshProducts()
}
