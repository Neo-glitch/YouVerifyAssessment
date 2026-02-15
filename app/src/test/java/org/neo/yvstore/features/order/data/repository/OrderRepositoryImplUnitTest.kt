package org.neo.yvstore.features.order.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.order.data.datasource.remote.OrderRemoteDatasource
import org.neo.yvstore.features.order.domain.model.OrderLineItem
import com.google.common.truth.Truth.assertThat

class OrderRepositoryImplUnitTest {

    private val remoteDatasource: OrderRemoteDatasource = mockk()
    private val userManager: UserManager = mockk()
    private lateinit var repository: OrderRepositoryImpl

    private val user = User("u1", "a@b.com", "John", "Doe")
    private val items = listOf(OrderLineItem("p1", "Shoe", 99.0, 2))

    @Before
    fun setUp() {
        repository = OrderRepositoryImpl(remoteDatasource, userManager)
    }

    @Test
    fun `placeOrder should return success with orderId`() = runTest {
        coEvery { userManager.getUser() } returns user
        coEvery { remoteDatasource.createOrder(any()) } returns "order123"

        val result = repository.placeOrder(198.0, "123 Main, City", items)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo("order123")
        coVerify { remoteDatasource.createOrder(match { request ->
            request.userId == "u1" &&
            request.totalAmount == 198.0 &&
            request.shippingAddress == "123 Main, City" &&
            request.status == "confirmed" &&
            request.items.size == 1 &&
            request.items[0].productId == "p1"
        }) }
    }

    @Test
    fun `placeOrder should return error when user is null`() = runTest {
        coEvery { userManager.getUser() } returns null

        val result = repository.placeOrder(198.0, "123 Main, City", items)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("User not found")
    }

    @Test
    fun `placeOrder should return error when remote throws`() = runTest {
        coEvery { userManager.getUser() } returns user
        coEvery { remoteDatasource.createOrder(any()) } throws RuntimeException("network")

        val result = repository.placeOrder(198.0, "123 Main, City", items)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }
}
