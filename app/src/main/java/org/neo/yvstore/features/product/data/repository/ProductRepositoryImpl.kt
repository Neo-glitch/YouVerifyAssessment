package org.neo.yvstore.features.product.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.common.util.ExceptionHandler
import org.neo.yvstore.core.database.dao.ProductDao
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.data.datasource.remote.ProductRemoteDatasource
import org.neo.yvstore.features.product.data.mapper.toEntity
import org.neo.yvstore.features.product.data.mapper.toProduct
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val remoteDatasource: ProductRemoteDatasource,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(count: Int): Flow<Resource<List<Product>>> {
        return productDao.getProducts(count)
            .map { entities ->
                Resource.Success(entities.map { it.toProduct() }) as Resource<List<Product>>
            }
            .catch { e ->
                emit(Resource.Error(ExceptionHandler.getErrorMessage(e)))
            }
    }

    override suspend fun refreshProducts(): Resource<Unit> {
        return try {
            val productDtos = remoteDatasource.getProducts()
            val entities = productDtos.map { it.toEntity() }
            productDao.insertProducts(entities)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }
}
