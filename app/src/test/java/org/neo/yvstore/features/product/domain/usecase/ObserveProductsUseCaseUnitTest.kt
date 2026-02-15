package org.neo.yvstore.features.product.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.repository.ProductRepository
import com.google.common.truth.Truth.assertThat

class ObserveProductsUseCaseUnitTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: ObserveProductsUseCase

    @Before
    fun setUp() {
        useCase = ObserveProductsUseCase(repository)
    }

    @Test
    fun `invoke with count forwards exact flow from repository`() {
        val flow = flowOf(Resource.Success(emptyList<Product>()))
        every { repository.observeProducts(10) } returns flow

        val result = useCase(count = 10)

        assertThat(result).isSameInstanceAs(flow)
        verify(exactly = 1) { repository.observeProducts(10) }
    }

    @Test
    fun `invoke with null count forwards to repository`() {
        val flow = flowOf(Resource.Success(emptyList<Product>()))
        every { repository.observeProducts(null) } returns flow

        val result = useCase(count = null)

        assertThat(result).isSameInstanceAs(flow)
        verify(exactly = 1) { repository.observeProducts(null) }
    }
}
