package org.neo.yvstore.features.cart.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository
import com.google.common.truth.Truth.assertThat

class AddCartItemUseCaseUnitTest {

    private val repository: CartRepository = mockk()
    private lateinit var useCase: AddCartItemUseCase

    @Before
    fun setUp() {
        useCase = AddCartItemUseCase(repository)
    }

    @Test
    fun `invoke returns success from repository`() = runTest {
        val entity = CartItemEntity(
            id = 0, productId = "p1", productName = "Shoe",
            productImageUrl = "url", unitPrice = 99.0, quantity = 1
        )
        coEvery { repository.addCartItem(entity) } returns Resource.Success(Unit)

        val result = useCase(entity)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { repository.addCartItem(entity) }
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        val entity = CartItemEntity(
            id = 0, productId = "p1", productName = "Shoe",
            productImageUrl = "url", unitPrice = 99.0, quantity = 1
        )
        coEvery { repository.addCartItem(entity) } returns Resource.Error("A local storage error occurred")

        val result = useCase(entity)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("A local storage error occurred")
    }
}
