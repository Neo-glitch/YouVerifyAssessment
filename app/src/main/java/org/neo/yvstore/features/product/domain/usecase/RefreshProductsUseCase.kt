package org.neo.yvstore.features.product.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.repository.ProductRepository

class RefreshProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repository.refreshProducts()
}
