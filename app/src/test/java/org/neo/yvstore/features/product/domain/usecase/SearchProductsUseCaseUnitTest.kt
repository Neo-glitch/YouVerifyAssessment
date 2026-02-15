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

class SearchProductsUseCaseUnitTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: SearchProductsUseCase

    @Before
    fun setUp() {
        useCase = SearchProductsUseCase(repository)
    }

    @Test
    fun `invoke returns success from repository`() = runTest {
        val products = listOf(
            Product("1", "Shoe", "Desc", 99.0, "url", 4.5, 10, "2024-01-01")
        )
        coEvery { repository.searchProducts("Shoe") } returns Resource.Success(products)

        val result = useCase(query = "Shoe")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(products)
        coVerify(exactly = 1) { repository.searchProducts("Shoe") }
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        coEvery { repository.searchProducts("test") } returns Resource.Error("Network error")

        val result = useCase(query = "test")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Network error")
    }
}
