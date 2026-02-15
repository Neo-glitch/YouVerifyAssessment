package org.neo.yvstore.features.cart.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository
import com.google.common.truth.Truth.assertThat

class ObserveCartItemByProductIdUseCaseUnitTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: ObserveCartItemByProductIdUseCase

    @Before
    fun setUp() {
        useCase = ObserveCartItemByProductIdUseCase(repository)
    }

    @Test
    fun `invoke should forward exact flow from repository`() {
        val flow = flowOf(Resource.Success<CartItem?>(null))
        every { repository.observeCartItemByProductId("p1") } returns flow

        val result = useCase(productId = "p1")

        assertThat(result).isSameInstanceAs(flow)
        verify(exactly = 1) { repository.observeCartItemByProductId("p1") }
    }
}
