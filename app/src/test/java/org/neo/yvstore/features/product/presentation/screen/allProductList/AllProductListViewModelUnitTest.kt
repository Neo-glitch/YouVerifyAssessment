package org.neo.yvstore.features.product.presentation.screen.allProductList

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.ObserveProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class AllProductListViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeProductsUseCase: ObserveProductsUseCase = mockk()
    private val refreshProductsUseCase: RefreshProductsUseCase = mockk()

    private val product = Product("1", "Shoe", "Desc", 99.0, "url", 4.5, 10, "2024-01-01")

    private fun createViewModel(): AllProductListViewModel {
        return AllProductListViewModel(observeProductsUseCase, refreshProductsUseCase)
    }

    @Test
    fun `init loads all products with null count`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.products).hasSize(1)
        assertThat(state.loadState).isEqualTo(AllProductListLoadState.Loaded)
    }

    @Test
    fun `init with empty cache and refresh error shows error`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshProductsUseCase() } returns Resource.Error("Network error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(AllProductListLoadState.Error::class.java)
    }

    @Test
    fun `init with cached products and refresh error keeps products visible`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Error("Timeout")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.products).hasSize(1)
    }
}
