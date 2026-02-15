package org.neo.yvstore.features.product.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.database.model.ProductEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.dao.TestProductDao
import org.neo.yvstore.testdoubles.datasource.TestProductRemoteDatasource

class ProductRepositoryImplIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: ProductRepositoryImpl
    private lateinit var testDatasource: TestProductRemoteDatasource
    private lateinit var testDao: TestProductDao

    @Before
    fun setup() {
        testDatasource = TestProductRemoteDatasource()
        testDao = TestProductDao()
        repository = ProductRepositoryImpl(testDatasource, testDao)
    }

    @Test
    fun `observeProducts should emit mapped products from dao`() = runTest {
        // Arrange
        val entities = listOf(
            createTestProductEntity(id = "product-1", name = "Laptop"),
            createTestProductEntity(id = "product-2", name = "Mouse")
        )
        testDao.insertProducts(entities)

        // Act & Assert
        repository.observeProducts().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val successResult = result as Resource.Success
            assertThat(successResult.data).hasSize(2)
            assertThat(successResult.data[0].id).isEqualTo("product-1")
            assertThat(successResult.data[0].name).isEqualTo("Laptop")
            assertThat(successResult.data[1].id).isEqualTo("product-2")
            assertThat(successResult.data[1].name).isEqualTo("Mouse")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `searchProducts should return mapped products from remote`() = runTest {
        // Arrange
        val productDtos = listOf(
            createTestProductDto(id = "product-1", name = "Laptop Pro"),
            createTestProductDto(id = "product-2", name = "Laptop Air")
        )
        testDatasource.products = productDtos

        // Act
        val result = repository.searchProducts("Laptop")

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data).hasSize(2)
        assertThat(successResult.data[0].name).isEqualTo("Laptop Pro")
        assertThat(successResult.data[1].name).isEqualTo("Laptop Air")
    }

    @Test
    fun `searchProducts should return Error when remote throws exception`() = runTest {
        // Arrange
        testDatasource.error = Exception("Network error")

        // Act
        val result = repository.searchProducts("query")

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("An unexpected error occurred")
    }

    @Test
    fun `refreshProducts should clear and reinsert when remote returns products`() = runTest {
        // Arrange - pre-populate DAO with old data
        testDao.insertProducts(listOf(createTestProductEntity(id = "old-product", name = "Old Item")))

        // Configure remote with new data
        val newProducts = listOf(
            createTestProductDto(id = "product-1", name = "New Item 1"),
            createTestProductDto(id = "product-2", name = "New Item 2")
        )
        testDatasource.products = newProducts

        // Act
        val result = repository.refreshProducts()

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify DAO contains only new products
        val daoProduct = testDao.getProductById("old-product")
        assertThat(daoProduct).isNull() // Old product cleared

        val newProduct1 = testDao.getProductById("product-1")
        assertThat(newProduct1).isNotNull()
        assertThat(newProduct1?.name).isEqualTo("New Item 1")

        val newProduct2 = testDao.getProductById("product-2")
        assertThat(newProduct2).isNotNull()
        assertThat(newProduct2?.name).isEqualTo("New Item 2")
    }

    @Test
    fun `refreshProducts should clear dao when remote returns empty list`() = runTest {
        // Arrange - pre-populate DAO
        val existingEntity = createTestProductEntity(id = "product-1", name = "Existing Item")
        testDao.insertProducts(listOf(existingEntity))

        // Configure remote to return empty list
        testDatasource.products = emptyList()

        // Act
        val result = repository.refreshProducts()

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify DAO is cleared (product removed)
        val product = testDao.getProductById("product-1")
        assertThat(product).isNull()
    }

    @Test
    fun `refreshProducts should return Error when remote throws exception`() = runTest {
        // Arrange
        testDatasource.error = Exception("Failed to fetch products")

        // Act
        val result = repository.refreshProducts()

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("An unexpected error occurred")
    }

    @Test
    fun `getProduct should return mapped product when found in dao`() = runTest {
        // Arrange
        val entity = createTestProductEntity(id = "product-1", name = "Gaming Mouse")
        testDao.insertProducts(listOf(entity))

        // Act
        val result = repository.getProduct("product-1")

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data.id).isEqualTo("product-1")
        assertThat(successResult.data.name).isEqualTo("Gaming Mouse")
        assertThat(successResult.data.price).isEqualTo(49.99)
    }

    @Test
    fun `getProduct should return Error when not found in dao`() = runTest {
        // Arrange - empty DAO

        // Act
        val result = repository.getProduct("non-existent-id")

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("Product not found")
    }

    // Helper functions to create test data
    private fun createTestProductEntity(
        id: String,
        name: String,
        price: Double = 49.99,
        description: String = "Test description",
        imageUrl: String = "https://example.com/image.jpg",
        rating: Double = 4.5,
        reviewCount: Int = 10,
        createdAt: String = "2024-01-01T00:00:00Z"
    ) = ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        createdAt = createdAt
    )

    private fun createTestProductDto(
        id: String,
        name: String,
        price: Double = 49.99,
        description: String = "Test description",
        imageUrl: String = "https://example.com/image.jpg",
        rating: Double = 4.5,
        reviewCount: Int = 10,
        createdAt: String = "2024-01-01T00:00:00Z",
        searchName: String = name.lowercase()
    ) = ProductDto(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        createdAt = createdAt,
        searchName = searchName
    )
}
