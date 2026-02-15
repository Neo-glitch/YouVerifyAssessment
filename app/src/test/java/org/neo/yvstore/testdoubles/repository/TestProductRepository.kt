package org.neo.yvstore.testdoubles.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository

class TestProductRepository : ProductRepository {
    private val products = mutableListOf<Product>()
    private val productsFlow = MutableSharedFlow<List<Product>>(replay = 1)

    var refreshResult: Resource<Unit> = Resource.Success(Unit)
    var searchResult: Resource<List<Product>>? = null
    var getProductResult: Resource<Product>? = null

    init {
        productsFlow.tryEmit(emptyList())
    }

    override fun observeProducts(count: Int?): Flow<Resource<List<Product>>> {
        return productsFlow.map { all ->
            val filtered = if (count == null) all else all.take(count)
            Resource.Success(filtered)
        }
    }

    override suspend fun searchProducts(query: String): Resource<List<Product>> {
        return searchResult ?: Resource.Success(
            products.filter { it.name.contains(query, ignoreCase = true) }
        )
    }

    override suspend fun refreshProducts(): Resource<Unit> = refreshResult

    override suspend fun getProduct(id: String): Resource<Product> {
        return getProductResult ?: run {
            products.find { it.id == id }?.let { Resource.Success(it) }
                ?: Resource.Error("Product not found")
        }
    }

    suspend fun emit(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        productsFlow.emit(products)
    }
}
