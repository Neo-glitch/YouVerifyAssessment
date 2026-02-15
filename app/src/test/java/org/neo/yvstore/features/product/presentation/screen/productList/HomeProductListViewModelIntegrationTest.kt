package org.neo.yvstore.features.product.presentation.screen.productList

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemCountUseCase
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.ObserveProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestCartRepository
import org.neo.yvstore.testdoubles.repository.TestProductRepository

class HomeProductListViewModelIntegrationTest {

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
        )
    )

    @Test
    fun `init should load cached products and set Loaded state`() = runTest {
        // Arrange - emit products BEFORE constructing ViewModel
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.refreshResult = Resource.Success(Unit)

        testProductRepository.emit(createTestProducts())

        // Act - construct ViewModel (init{} runs here)
        val viewModel = HomeProductListViewModel(
            observeProductsUseCase = ObserveProductsUseCase(testProductRepository),
            refreshProductsUseCase = RefreshProductsUseCase(testProductRepository),
            observeCartItemCountUseCase = ObserveCartItemCountUseCase(testCartRepository)
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(HomeProductListLoadState.Loaded)
        assertThat(state.products).hasSize(2)
        assertThat(state.products[0].name).isEqualTo("Product 1")
        assertThat(state.products[1].name).isEqualTo("Product 2")
    }

    @Test
    fun `init should show Error when cache fails and no products`() = runTest {
        // Arrange - don't emit products, set refresh to fail
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.refreshResult = Resource.Error("Network error")

        // Act - construct ViewModel (init{} runs here)
        val viewModel = HomeProductListViewModel(
            observeProductsUseCase = ObserveProductsUseCase(testProductRepository),
            refreshProductsUseCase = RefreshProductsUseCase(testProductRepository),
            observeCartItemCountUseCase = ObserveCartItemCountUseCase(testCartRepository)
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(HomeProductListLoadState.Error::class.java)
        assertThat((state.loadState as HomeProductListLoadState.Error).message).isEqualTo("Network error")
        assertThat(state.products).isEmpty()
    }

    @Test
    fun `init should send toast when refresh fails but cached products exist`() = runTest {
        // Arrange - emit products but set refresh to fail
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.refreshResult = Resource.Error("Failed to refresh")

        testProductRepository.emit(createTestProducts())

        // Act - construct ViewModel and collect events
        val viewModel = HomeProductListViewModel(
            observeProductsUseCase = ObserveProductsUseCase(testProductRepository),
            refreshProductsUseCase = RefreshProductsUseCase(testProductRepository),
            observeCartItemCountUseCase = ObserveCartItemCountUseCase(testCartRepository)
        )

        // Assert
        viewModel.uiEvent.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(HomeProductListUiEvent.ShowToast::class.java)
            assertThat((event as HomeProductListUiEvent.ShowToast).message).isEqualTo("Failed to refresh")

            cancelAndConsumeRemainingEvents()
        }

        // Products should still be shown
        assertThat(viewModel.uiState.value.products).hasSize(2)
    }

    @Test
    fun `init should observe cart item count updates`() = runTest {
        // Arrange
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.refreshResult = Resource.Success(Unit)

        testProductRepository.emit(createTestProducts())

        // Act - construct ViewModel
        val viewModel = HomeProductListViewModel(
            observeProductsUseCase = ObserveProductsUseCase(testProductRepository),
            refreshProductsUseCase = RefreshProductsUseCase(testProductRepository),
            observeCartItemCountUseCase = ObserveCartItemCountUseCase(testCartRepository)
        )

        // Initially no cart items
        assertThat(viewModel.uiState.value.cartItemCount).isEqualTo(0)

        // Add cart item
        testCartRepository.addCartItem(
            CartItemEntity(
                id = 0,
                productId = "p1",
                productName = "Product 1",
                productImageUrl = "url1",
                unitPrice = 10.0,
                quantity = 1
            )
        )

        // Assert - cart item count should update
        assertThat(viewModel.uiState.value.cartItemCount).isEqualTo(1)
    }
}
