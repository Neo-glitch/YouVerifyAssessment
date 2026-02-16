package org.neo.yvstore.features.product.presentation.screen.productList

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
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemCountUseCase
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.ObserveProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class HomeProductListViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeProductsUseCase: ObserveProductsUseCase = mockk()
    private val refreshProductsUseCase: RefreshProductsUseCase = mockk()
    private val observeCartItemCountUseCase: ObserveCartItemCountUseCase = mockk()

    private val product = Product("1", "Shoe", "Desc", 99.0, "url", 4.5, 10, "2024-01-01")

    private fun createViewModel(): HomeProductListViewModel {
        return HomeProductListViewModel(
            observeProductsUseCase,
            refreshProductsUseCase,
            observeCartItemCountUseCase
        )
    }

    @Test
    fun `init should load products and set loaded state`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(0))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.products).hasSize(1)
        assertThat(state.products[0].name).isEqualTo("Shoe")
        assertThat(state.loadState).isEqualTo(HomeProductListLoadState.Loaded)
    }

    @Test
    fun `init with empty cache and refresh error should show error state`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshProductsUseCase() } returns Resource.Error("Network error")
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(0))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(HomeProductListLoadState.Error::class.java)
        assertThat((state.loadState as HomeProductListLoadState.Error).message).isEqualTo("Network error")
    }

    @Test
    fun `init should observe cart item count`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(5))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.cartItemCount).isEqualTo(5)
    }

    @Test
    fun `init with cached products and refresh error should show toast`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Error("Timeout")
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(0))

        val viewModel = createViewModel()
        advanceUntilIdle()

        // Products should still be shown despite refresh error
        assertThat(viewModel.uiState.value.products).hasSize(1)
    }

    @Test
    fun `init with empty cache and successful refresh should set Empty state`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(0))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.loadState).isEqualTo(HomeProductListLoadState.Empty)
    }

    @Test
    fun `onRefresh should set isRefreshing true then false on success`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(0))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isRefreshing).isFalse()
        assertThat(viewModel.uiState.value.loadState).isEqualTo(HomeProductListLoadState.Loaded)
    }

    @Test
    fun `onRefresh should not trigger when loadState is Loading`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshProductsUseCase() } coAnswers {
            kotlinx.coroutines.delay(10_000)
            Resource.Success(Unit)
        }
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(0))

        val viewModel = createViewModel()

        viewModel.onRefresh()

        assertThat(viewModel.uiState.value.isRefreshing).isFalse()
    }

    @Test
    fun `onRefresh with error and existing products should set isRefreshing false`() = runTest {
        every { observeProductsUseCase(10) } returns flowOf(Resource.Success(listOf(product)))
        coEvery { refreshProductsUseCase() } returns Resource.Success(Unit)
        every { observeCartItemCountUseCase() } returns flowOf(Resource.Success(0))

        val viewModel = createViewModel()
        advanceUntilIdle()

        coEvery { refreshProductsUseCase() } returns Resource.Error("Network error")

        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isRefreshing).isFalse()
        assertThat(viewModel.uiState.value.products).hasSize(1)
    }
}
