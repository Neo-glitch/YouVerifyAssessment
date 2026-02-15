package org.neo.yvstore.testdoubles.datasource

import org.neo.yvstore.features.product.data.datasource.remote.ProductRemoteDatasource
import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto

class TestProductRemoteDatasource : ProductRemoteDatasource {
    var products: List<ProductDto> = emptyList()
    var error: Exception? = null

    override suspend fun getProducts(): List<ProductDto> {
        error?.let { throw it }
        return products
    }

    override suspend fun searchProducts(query: String): List<ProductDto> {
        error?.let { throw it }
        return if (query.isBlank()) products
        else products.filter { it.name.startsWith(query, ignoreCase = true) }
    }
}
