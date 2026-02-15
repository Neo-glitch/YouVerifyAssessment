package org.neo.yvstore.features.product.presentation.screen.allProductList

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.ObserveProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestProductRepository

class AllProductListViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createTestProducts() = listOf(
        Product(
            id = "p1",
            name = "Product 1",
            description = "Description 1",
            price = 10.0,
            imageUrl = "url1",
            rating = 4.5,
            reviewCount = 100,
            createdAt = "2024-01-01"
        ),
        Product(
            id = "p2",
            name = "Product 2",
            description = "Description 2",
            price = 20.0,
            imageUrl = "url2",
            rating = 4.0,
            reviewCount = 50,
            createdAt = "2024-01-02"
        ),
        Product(
            id = "p3",
            name = "Product 3",
            description = "Description 3",
            price = 30.0,
            imageUrl = "url3",
            rating = 3.5,
            reviewCount = 25,
            createdAt = "2024-01-03"
        )
    )

    @Test
    fun `init should load cached products and set Loaded state`() = runTest {
        // Arrange - emit products BEFORE constructing ViewModel
        val testProductRepository = TestProductRepository()
        testProductRepository.refreshResult = Resource.Success(Unit)

        testProductRepository.emit(createTestProducts())

        // Act - construct ViewModel (init{} runs here)
        val viewModel = AllProductListViewModel(
            observeProductsUseCase = ObserveProductsUseCase(testProductRepository),
            refreshProductsUseCase = RefreshProductsUseCase(testProductRepository)
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(AllProductListLoadState.Loaded)
        assertThat(state.products).hasSize(3)
        assertThat(state.products[0].name).isEqualTo("Product 1")
    }

    @Test
    fun `init should show Error when cache fails and no products`() = runTest {
        // Arrange - don't emit products, set refresh to fail
        val testProductRepository = TestProductRepository()
        testProductRepository.refreshResult = Resource.Error("Network error")

        // Act - construct ViewModel (init{} runs here)
        val viewModel = AllProductListViewModel(
            observeProductsUseCase = ObserveProductsUseCase(testProductRepository),
            refreshProductsUseCase = RefreshProductsUseCase(testProductRepository)
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(AllProductListLoadState.Error::class.java)
        assertThat((state.loadState as AllProductListLoadState.Error).message).isEqualTo("Network error")
        assertThat(state.products).isEmpty()
    }

    @Test
    fun `init should send toast when refresh fails but cached products exist`() = runTest {
        // Arrange - emit products but set refresh to fail
        val testProductRepository = TestProductRepository()
        testProductRepository.refreshResult = Resource.Error("Failed to refresh")

        testProductRepository.emit(createTestProducts())

        // Act - construct ViewModel and collect events
        val viewModel = AllProductListViewModel(
            observeProductsUseCase = ObserveProductsUseCase(testProductRepository),
            refreshProductsUseCase = RefreshProductsUseCase(testProductRepository)
        )

        // Assert
        viewModel.uiEvent.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(AllProductListUiEvent.ShowToast::class.java)
            assertThat((event as AllProductListUiEvent.ShowToast).message).isEqualTo("Failed to refresh")

            cancelAndConsumeRemainingEvents()
        }

        // Products should still be shown
        assertThat(viewModel.uiState.value.products).hasSize(3)
    }
}
