package org.neo.yvstore.features.product.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository

class GetProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: String): Resource<Product> =
        repository.getProduct(id)
}
