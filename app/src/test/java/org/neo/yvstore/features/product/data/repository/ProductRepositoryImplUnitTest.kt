package org.neo.yvstore.features.product.data.repository

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.database.dao.ProductDao
import org.neo.yvstore.core.database.model.ProductEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.data.datasource.remote.ProductRemoteDatasource
import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto
import com.google.common.truth.Truth.assertThat

class ProductRepositoryImplUnitTest {

    private val remoteDatasource: ProductRemoteDatasource = mockk()
    private val productDao: ProductDao = mockk()
    private lateinit var repository: ProductRepositoryImpl

    private val entity = ProductEntity(
        id = "1", name = "Shoe", description = "Desc", price = 99.0,
        imageUrl = "url", rating = 4.5, reviewCount = 10, createdAt = "2024-01-01"
    )

    @Before
    fun setUp() {
        repository = ProductRepositoryImpl(remoteDatasource, productDao)
    }

    // ── observeProducts ──

    @Test
    fun `observeProducts should emit success with mapped products`() = runTest {
        every { productDao.observeProducts(10) } returns flowOf(listOf(entity))

        repository.observeProducts(10).test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val products = (result as Resource.Success).data
            assertThat(products).hasSize(1)
            assertThat(products[0].id).isEqualTo("1")
            assertThat(products[0].name).isEqualTo("Shoe")
            awaitComplete()
        }
    }

    @Test
    fun `observeProducts should emit error when dao throws`() = runTest {
        every { productDao.observeProducts(null) } returns flow {
            throw RuntimeException("db error")
        }

        repository.observeProducts(null).test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
            awaitComplete()
        }
    }

    // ── searchProducts ──

    @Test
    fun `searchProducts should return success with mapped products`() = runTest {
        val dto = ProductDto(
            id = "1", name = "Shoe", description = "Desc", price = 99.0,
            imageUrl = "url", rating = 4.5, reviewCount = 10, createdAt = "2024-01-01"
        )
        coEvery { remoteDatasource.searchProducts("Shoe") } returns listOf(dto)

        val result = repository.searchProducts("Shoe")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val products = (result as Resource.Success).data
        assertThat(products).hasSize(1)
        assertThat(products[0].id).isEqualTo("1")
    }

    @Test
    fun `searchProducts should return error when remote throws`() = runTest {
        coEvery { remoteDatasource.searchProducts("test") } throws RuntimeException("network")

        val result = repository.searchProducts("test")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }

    // ── refreshProducts ──

    @Test
    fun `refreshProducts should clear then insert and return success`() = runTest {
        val dto = ProductDto(
            id = "1", name = "Shoe", description = "Desc", price = 99.0,
            imageUrl = "url", rating = 4.5, reviewCount = 10, createdAt = "2024-01-01"
        )
        coEvery { remoteDatasource.getProducts() } returns listOf(dto)
        coEvery { productDao.clearAllProducts() } returns Unit
        coEvery { productDao.insertProducts(any()) } returns Unit

        val result = repository.refreshProducts()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerifyOrder {
            productDao.clearAllProducts()
            productDao.insertProducts(any())
        }
    }

    @Test
    fun `refreshProducts should return error when remote throws`() = runTest {
        coEvery { remoteDatasource.getProducts() } throws RuntimeException("timeout")

        val result = repository.refreshProducts()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }

    // ── getProduct ──

    @Test
    fun `getProduct should return success when found`() = runTest {
        coEvery { productDao.getProductById("1") } returns entity

        val result = repository.getProduct("1")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data.id).isEqualTo("1")
    }

    @Test
    fun `getProduct should return error when not found`() = runTest {
        coEvery { productDao.getProductById("999") } returns null

        val result = repository.getProduct("999")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Product not found")
    }

    @Test
    fun `getProduct should return error when dao throws`() = runTest {
        coEvery { productDao.getProductById("1") } throws RuntimeException("db")

        val result = repository.getProduct("1")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }
}
