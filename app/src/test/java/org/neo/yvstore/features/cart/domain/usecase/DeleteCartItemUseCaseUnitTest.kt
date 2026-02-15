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

class DeleteCartItemUseCaseUnitTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: DeleteCartItemUseCase

    @Before
    fun setUp() {
        useCase = DeleteCartItemUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        coEvery { repository.deleteCartItem(1L) } returns Resource.Success(Unit)

        val result = useCase(id = 1L)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { repository.deleteCartItem(1L) }
    }

    @Test
    fun `invoke should return error from repository`() = runTest {
        coEvery { repository.deleteCartItem(1L) } returns Resource.Error("A local storage error occurred")

        val result = useCase(id = 1L)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("A local storage error occurred")
    }
}
