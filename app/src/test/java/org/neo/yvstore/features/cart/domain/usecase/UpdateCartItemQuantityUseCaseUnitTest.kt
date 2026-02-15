package org.neo.yvstore.features.cart.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository
import com.google.common.truth.Truth.assertThat

class UpdateCartItemQuantityUseCaseUnitTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: UpdateCartItemQuantityUseCase

    @Before
    fun setUp() {
        useCase = UpdateCartItemQuantityUseCase(repository)
    }

    @Test
    fun `invoke returns success from repository`() = runTest {
        coEvery { repository.updateQuantity(1L, 5) } returns Resource.Success(Unit)

        val result = useCase(id = 1L, quantity = 5)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { repository.updateQuantity(1L, 5) }
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        coEvery { repository.updateQuantity(1L, 5) } returns Resource.Error("Database error")

        val result = useCase(id = 1L, quantity = 5)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Database error")
    }
}
