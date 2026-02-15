package org.neo.yvstore.features.product.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.repository.ProductRepository
import com.google.common.truth.Truth.assertThat

class RefreshProductsUseCaseUnitTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: RefreshProductsUseCase

    @Before
    fun setUp() {
        useCase = RefreshProductsUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        coEvery { repository.refreshProducts() } returns Resource.Success(Unit)

        val result = useCase()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { repository.refreshProducts() }
    }

    @Test
    fun `invoke should return error from repository`() = runTest {
        coEvery { repository.refreshProducts() } returns Resource.Error("Timeout")

        val result = useCase()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Timeout")
    }
}
