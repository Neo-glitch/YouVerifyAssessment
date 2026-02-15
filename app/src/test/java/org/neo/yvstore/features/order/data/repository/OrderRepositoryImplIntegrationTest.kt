package org.neo.yvstore.features.order.data.repository

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.neo.yvstore.core.data.UserManagerImpl
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.order.domain.model.OrderLineItem
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.TestAppCache
import org.neo.yvstore.testdoubles.datasource.TestOrderRemoteDatasource
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

class OrderRepositoryImplIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: OrderRepositoryImpl
    private lateinit var testDatasource: TestOrderRemoteDatasource
    private lateinit var userManager: UserManagerImpl
    private lateinit var testCache: TestAppCache

    @Before
    fun setup() {
        testDatasource = TestOrderRemoteDatasource()
        testCache = TestAppCache()
        userManager = UserManagerImpl(testCache)
        repository = OrderRepositoryImpl(testDatasource, userManager)
    }

    @Test
    fun `placeOrder should return Success with orderId when user logged in`() = runTest {
        // Arrange - save logged-in user
        val user = createTestUser(uid = "user-123")
        userManager.saveUser(user)

        val orderItems = listOf(
            createTestOrderLineItem(productId = "product-1", productName = "Laptop", quantity = 1),
            createTestOrderLineItem(productId = "product-2", productName = "Mouse", quantity = 2)
        )
        testDatasource.orderId = "order-abc123"

        // Act
        val result = repository.placeOrder(
            totalAmount = 1299.97,
            shippingAddress = "123 Main St, New York, NY 10001",
            items = orderItems
        )

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data).isEqualTo("order-abc123")
    }

    @Test
    fun `placeOrder should return Error when user not logged in`() = runTest {
        // Arrange - no user saved

        val orderItems = listOf(
            createTestOrderLineItem(productId = "product-1", productName = "Laptop", quantity = 1)
        )

        // Act
        val result = repository.placeOrder(
            totalAmount = 999.99,
            shippingAddress = "123 Main St, New York, NY 10001",
            items = orderItems
        )

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("User not found")
    }

    @Test
    fun `placeOrder should return Error when remote throws exception`() = runTest {
        // Arrange - save logged-in user
        val user = createTestUser(uid = "user-123")
        userManager.saveUser(user)

        testDatasource.error = Exception("Payment processing failed")

        val orderItems = listOf(
            createTestOrderLineItem(productId = "product-1", productName = "Laptop", quantity = 1)
        )

        // Act
        val result = repository.placeOrder(
            totalAmount = 999.99,
            shippingAddress = "123 Main St, New York, NY 10001",
            items = orderItems
        )

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("An unexpected error occurred")
    }

    // Helper functions to create test data
    private fun createTestUser(
        uid: String = "test-user-id",
        email: String = "test@example.com",
        firstName: String = "John",
        lastName: String = "Doe"
    ) = User(
        uid = uid,
        email = email,
        firstName = firstName,
        lastName = lastName
    )

    private fun createTestOrderLineItem(
        productId: String,
        productName: String,
        unitPrice: Double = 99.99,
        quantity: Int
    ) = OrderLineItem(
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        quantity = quantity
    )
}
