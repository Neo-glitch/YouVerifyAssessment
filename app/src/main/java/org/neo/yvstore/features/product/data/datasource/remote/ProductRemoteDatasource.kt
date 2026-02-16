package org.neo.yvstore.features.product.data.datasource.remote

import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto

interface ProductRemoteDatasource {
    suspend fun getProducts(): List<ProductDto>

    suspend fun searchProducts(query: String = ""): List<ProductDto>
}
