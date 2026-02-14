package org.neo.yvstore.features.product.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository


class SearchProductsUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(query: String = ""): Resource<List<Product>> =
        repository.searchProducts(query)
}
