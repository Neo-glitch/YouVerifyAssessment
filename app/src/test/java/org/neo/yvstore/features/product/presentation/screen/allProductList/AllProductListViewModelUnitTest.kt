package org.neo.yvstore.features.product.presentation.screen.allProductList

import app.cash.turbine.test
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
    fun `init should load all products with null count`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.products).hasSize(1)
        assertThat(state.loadState).isEqualTo(AllProductListLoadState.Loaded)
    }

    @Test
    fun `init with empty cache and refresh error should show error`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshProductsUseCase() } returns Resource.Error("Network error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(AllProductListLoadState.Error::class.java)
    }

    @Test
    fun `init with cached products and refresh error should keep products visible`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Error("Timeout")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.products).hasSize(1)
    }

    @Test
    fun `init with empty cache and successful refresh should set Loaded state`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.loadState).isEqualTo(AllProductListLoadState.Loaded)
        assertThat(viewModel.uiState.value.products).isEmpty()
    }

    @Test
    fun `onRefresh should set isRefreshing true then false on success`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isRefreshing).isFalse()
        assertThat(viewModel.uiState.value.loadState).isEqualTo(AllProductListLoadState.Loaded)
    }

    @Test
    fun `onRefresh should not trigger when loadState is Loading`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshProductsUseCase() } coAnswers {
            kotlinx.coroutines.delay(10_000)
            Resource.Success(Unit)
        }

        val viewModel = createViewModel()

        viewModel.onRefresh()

        assertThat(viewModel.uiState.value.isRefreshing).isFalse()
    }

    @Test
    fun `onRefresh with error and existing products should set isRefreshing false`() = runTest {
        every { observeProductsUseCase(count = null) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        coEvery { refreshProductsUseCase() } returns Resource.Error("Network error")

        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isRefreshing).isFalse()
        assertThat(viewModel.uiState.value.products).hasSize(1)
    }
}
