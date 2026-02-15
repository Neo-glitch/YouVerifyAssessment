package org.neo.yvstore.features.product.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository
import com.google.common.truth.Truth.assertThat

class GetProductUseCaseUnitTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: GetProductUseCase

    @Before
    fun setUp() {
        useCase = GetProductUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        val product = Product("1", "Shoe", "Desc", 99.0, "url", 4.5, 10, "2024-01-01")
        coEvery { repository.getProduct("1") } returns Resource.Success(product)

        val result = useCase(id = "1")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(product)
        coVerify(exactly = 1) { repository.getProduct("1") }
    }

    @Test
    fun `invoke should return error from repository`() = runTest {
        coEvery { repository.getProduct("999") } returns Resource.Error("Product not found")

        val result = useCase(id = "999")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Product not found")
    }
}
