package org.neo.yvstore.features.product.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository

class ObserveProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(count: Int? = null): Flow<Resource<List<Product>>> =
        repository.observeProducts(count)
}
