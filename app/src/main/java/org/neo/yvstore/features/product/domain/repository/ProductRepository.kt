package org.neo.yvstore.features.product.domain.repository

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product

interface ProductRepository {
    fun getProducts(count: Int): Flow<Resource<List<Product>>>
    suspend fun refreshProducts(): Resource<Unit>
}
