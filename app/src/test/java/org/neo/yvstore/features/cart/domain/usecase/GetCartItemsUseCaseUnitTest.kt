package org.neo.yvstore.features.cart.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository
import com.google.common.truth.Truth.assertThat

class GetCartItemsUseCaseUnitTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: GetCartItemsUseCase

    @Before
    fun setUp() {
        useCase = GetCartItemsUseCase(repository)
    }

    @Test
    fun `invoke returns success from repository`() = runTest {
        val items = listOf(
            CartItem(1L, "p1", "Shoe", "url", 99.0, 2)
        )
        coEvery { repository.getCartItems() } returns Resource.Success(items)

        val result = useCase()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(items)
        coVerify(exactly = 1) { repository.getCartItems() }
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        coEvery { repository.getCartItems() } returns Resource.Error("Database error")

        val result = useCase()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Database error")
    }
}
