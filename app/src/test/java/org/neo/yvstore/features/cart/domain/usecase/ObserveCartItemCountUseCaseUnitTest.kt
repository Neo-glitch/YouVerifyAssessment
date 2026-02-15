package org.neo.yvstore.features.cart.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository
import com.google.common.truth.Truth.assertThat

class ObserveCartItemCountUseCaseUnitTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: ObserveCartItemCountUseCase

    @Before
    fun setUp() {
        useCase = ObserveCartItemCountUseCase(repository)
    }

    @Test
    fun `invoke should forward exact flow from repository`() {
        val flow = flowOf(Resource.Success(3))
        every { repository.observeCartItemCount() } returns flow

        val result = useCase()

        assertThat(result).isSameInstanceAs(flow)
        verify(exactly = 1) { repository.observeCartItemCount() }
    }
}
