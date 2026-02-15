package org.neo.yvstore.testdoubles.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.database.dao.ProductDao
import org.neo.yvstore.core.database.model.ProductEntity

class TestProductDao : ProductDao {
    private val products = mutableListOf<ProductEntity>()
    private val productsFlow = MutableSharedFlow<List<ProductEntity>>(replay = 1)

    init {
        productsFlow.tryEmit(emptyList())
    }

    override fun observeProducts(count: Int?): Flow<List<ProductEntity>> {
        return productsFlow.map { all ->
            if (count == null) all else all.take(count)
        }
    }

    override suspend fun insertProducts(products: List<ProductEntity>) {
        products.forEach { product ->
            this.products.removeIf { it.id == product.id }
            this.products.add(product)
        }
        this.products.sortByDescending { it.createdAt }
        productsFlow.emit(this.products.toList())
    }

    override suspend fun clearAllProducts() {
        products.clear()
        productsFlow.emit(emptyList())
    }

    override suspend fun getProductById(id: String): ProductEntity? {
        return products.find { it.id == id }
    }
}
