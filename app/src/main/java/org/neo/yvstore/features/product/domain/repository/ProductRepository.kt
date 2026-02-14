package org.neo.yvstore.features.product.domain.repository

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product

interface ProductRepository {
    fun observeProducts(count: Int? = null): Flow<Resource<List<Product>>>
    suspend fun searchProducts(query: String = ""): Resource<List<Product>>
    suspend fun refreshProducts(): Resource<Unit>
    suspend fun getProduct(id: String): Resource<Product>
}
