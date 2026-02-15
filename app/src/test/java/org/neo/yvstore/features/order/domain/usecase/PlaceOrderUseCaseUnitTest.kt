package org.neo.yvstore.features.order.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.order.domain.model.OrderLineItem
import org.neo.yvstore.features.order.domain.repository.OrderRepository
import com.google.common.truth.Truth.assertThat

class PlaceOrderUseCaseUnitTest {

    private val repository: OrderRepository = mockk()
    private lateinit var useCase: PlaceOrderUseCase

    @Before
    fun setUp() {
        useCase = PlaceOrderUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        val items = listOf(
            OrderLineItem("p1", "Shoe", 99.0, 2)
        )
        coEvery {
            repository.placeOrder(198.0, "123 Main, City", items)
        } returns Resource.Success("order123")

        val result = useCase(
            totalAmount = 198.0,
            shippingAddress = "123 Main, City",
            items = items
        )

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo("order123")
        coVerify(exactly = 1) {
            repository.placeOrder(198.0, "123 Main, City", items)
        }
    }

    @Test
    fun `invoke should return error from repository`() = runTest {
        val items = listOf(
            OrderLineItem("p1", "Shoe", 99.0, 2)
        )
        coEvery {
            repository.placeOrder(198.0, "123 Main, City", items)
        } returns Resource.Error("An unexpected error occurred")

        val result = useCase(
            totalAmount = 198.0,
            shippingAddress = "123 Main, City",
            items = items
        )

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }
}
