package org.neo.yvstore.features.cart.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.dao.TestCartItemDao

class CartRepositoryImplIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: CartRepositoryImpl
    private lateinit var testDao: TestCartItemDao

    @Before
    fun setup() {
        testDao = TestCartItemDao()
        repository = CartRepositoryImpl(testDao)
    }

    @Test
    fun `observeCartItems should emit mapped cart items from dao`() = runTest {
        // Arrange
        val entities = listOf(
            createTestCartItemEntity(productId = "product-1", productName = "Laptop", quantity = 1),
            createTestCartItemEntity(productId = "product-2", productName = "Mouse", quantity = 2)
        )
        entities.forEach { testDao.insertCartItem(it) }

        // Act & Assert
        repository.observeCartItems().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val successResult = result as Resource.Success
            assertThat(successResult.data).hasSize(2)
            assertThat(successResult.data[0].productName).isEqualTo("Laptop")
            assertThat(successResult.data[0].quantity).isEqualTo(1)
            assertThat(successResult.data[1].productName).isEqualTo("Mouse")
            assertThat(successResult.data[1].quantity).isEqualTo(2)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCartItems should return mapped cart items`() = runTest {
        // Arrange
        val entities = listOf(
            createTestCartItemEntity(productId = "product-1", productName = "Keyboard"),
            createTestCartItemEntity(productId = "product-2", productName = "Monitor")
        )
        entities.forEach { testDao.insertCartItem(it) }

        // Act
        val result = repository.getCartItems()

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data).hasSize(2)
        assertThat(successResult.data[0].productName).isEqualTo("Keyboard")
        assertThat(successResult.data[1].productName).isEqualTo("Monitor")
    }

    @Test
    fun `observeCartItemCount should emit count from dao`() = runTest {
        // Arrange
        val entities = listOf(
            createTestCartItemEntity(productId = "product-1", productName = "Item 1"),
            createTestCartItemEntity(productId = "product-2", productName = "Item 2"),
            createTestCartItemEntity(productId = "product-3", productName = "Item 3")
        )
        entities.forEach { testDao.insertCartItem(it) }

        // Act & Assert
        repository.observeCartItemCount().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val successResult = result as Resource.Success
            assertThat(successResult.data).isEqualTo(3)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `addCartItem should insert into dao and return Success`() = runTest {
        // Arrange
        val cartItem = createTestCartItemEntity(
            productId = "product-1",
            productName = "New Item",
            quantity = 2
        )

        // Act
        val result = repository.addCartItem(cartItem)

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify item in DAO
        val items = testDao.getCartItems()
        assertThat(items).hasSize(1)
        assertThat(items[0].productName).isEqualTo("New Item")
        assertThat(items[0].quantity).isEqualTo(2)
    }

    @Test
    fun `updateQuantity should update dao and return Success`() = runTest {
        // Arrange
        val cartItem = createTestCartItemEntity(
            productId = "product-1",
            productName = "Item",
            quantity = 1
        )
        testDao.insertCartItem(cartItem)
        val items = testDao.getCartItems()
        val itemId = items[0].id

        // Act
        val result = repository.updateQuantity(itemId, 5)

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify quantity updated
        val updatedItems = testDao.getCartItems()
        assertThat(updatedItems[0].quantity).isEqualTo(5)
    }

    @Test
    fun `deleteCartItem should delete from dao and return Success`() = runTest {
        // Arrange
        val cartItem = createTestCartItemEntity(
            productId = "product-1",
            productName = "Item to Delete"
        )
        testDao.insertCartItem(cartItem)
        val items = testDao.getCartItems()
        val itemId = items[0].id

        // Act
        val result = repository.deleteCartItem(itemId)

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify item deleted
        val remainingItems = testDao.getCartItems()
        assertThat(remainingItems).isEmpty()
    }

    @Test
    fun `deleteAllCartItems should clear dao and return Success`() = runTest {
        // Arrange - add multiple items
        val entities = listOf(
            createTestCartItemEntity(productId = "product-1", productName = "Item 1"),
            createTestCartItemEntity(productId = "product-2", productName = "Item 2"),
            createTestCartItemEntity(productId = "product-3", productName = "Item 3")
        )
        entities.forEach { testDao.insertCartItem(it) }

        // Act
        val result = repository.deleteAllCartItems()

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify all items deleted
        val remainingItems = testDao.getCartItems()
        assertThat(remainingItems).isEmpty()
    }

    @Test
    fun `observeCartItemByProductId should emit mapped item from dao`() = runTest {
        // Arrange
        val entities = listOf(
            createTestCartItemEntity(productId = "product-1", productName = "Laptop"),
            createTestCartItemEntity(productId = "product-2", productName = "Mouse")
        )
        entities.forEach { testDao.insertCartItem(it) }

        // Act & Assert
        repository.observeCartItemByProductId("product-1").test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val successResult = result as Resource.Success
            assertThat(successResult.data).isNotNull()
            assertThat(successResult.data?.productId).isEqualTo("product-1")
            assertThat(successResult.data?.productName).isEqualTo("Laptop")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `observeCartItemByProductId should emit null when product not in cart`() = runTest {
        // Arrange
        val entity = createTestCartItemEntity(productId = "product-1", productName = "Laptop")
        testDao.insertCartItem(entity)

        // Act & Assert
        repository.observeCartItemByProductId("non-existent-product").test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val successResult = result as Resource.Success
            assertThat(successResult.data).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    // Helper function to create test cart item entities
    private fun createTestCartItemEntity(
        productId: String,
        productName: String,
        quantity: Int = 1,
        unitPrice: Double = 99.99,
        productImageUrl: String = "https://example.com/image.jpg"
    ) = CartItemEntity(
        id = 0L, // Auto-generated by TestCartItemDao
        productId = productId,
        productName = productName,
        productImageUrl = productImageUrl,
        unitPrice = unitPrice,
        quantity = quantity
    )
}
