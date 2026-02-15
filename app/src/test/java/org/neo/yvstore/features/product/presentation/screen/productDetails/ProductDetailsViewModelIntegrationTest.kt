package org.neo.yvstore.features.product.presentation.screen.productDetails

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.usecase.AddCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemByProductIdUseCase
import org.neo.yvstore.features.cart.domain.usecase.UpdateCartItemQuantityUseCase
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.GetProductUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestCartRepository
import org.neo.yvstore.testdoubles.repository.TestProductRepository

class ProductDetailsViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testProductId = "p1"

    private fun createTestProduct() = Product(
        id = testProductId,
        name = "Test Product",
        description = "Test Description",
        price = 99.99,
        imageUrl = "test-url",
        rating = 4.5,
        reviewCount = 100,
        createdAt = "2024-01-01"
    )

    @Test
    fun `init should load product and set Loaded state`() = runTest {
        // Arrange
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.getProductResult = Resource.Success(createTestProduct())

        // Act - construct ViewModel (init{} runs here)
        val viewModel = ProductDetailsViewModel(
            getProductUseCase = GetProductUseCase(testProductRepository),
            observeCartItemByProductIdUseCase = ObserveCartItemByProductIdUseCase(testCartRepository),
            addCartItemUseCase = AddCartItemUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            productId = testProductId
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(ProductDetailsLoadState.Loaded)
        assertThat(state.product).isNotNull()
        assertThat(state.product?.name).isEqualTo("Test Product")
        assertThat(state.product?.price).isEqualTo(99.99)
        assertThat(state.quantity).isEqualTo(0)
    }

    @Test
    fun `init should set Error state when product not found`() = runTest {
        // Arrange
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.getProductResult = Resource.Error("Product not found")

        // Act - construct ViewModel (init{} runs here)
        val viewModel = ProductDetailsViewModel(
            getProductUseCase = GetProductUseCase(testProductRepository),
            observeCartItemByProductIdUseCase = ObserveCartItemByProductIdUseCase(testCartRepository),
            addCartItemUseCase = AddCartItemUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            productId = testProductId
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(ProductDetailsLoadState.Error::class.java)
        assertThat((state.loadState as ProductDetailsLoadState.Error).message).isEqualTo("Product not found")
    }

    @Test
    fun `init should observe cart item and update quantity`() = runTest {
        // Arrange - add cart item to repo BEFORE constructing ViewModel
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.getProductResult = Resource.Success(createTestProduct())

        testCartRepository.addCartItem(
            CartItemEntity(
                id = 0,
                productId = testProductId,
                productName = "Test Product",
                productImageUrl = "test-url",
                unitPrice = 99.99,
                quantity = 3
            )
        )

        // Act - construct ViewModel (init{} runs here)
        val viewModel = ProductDetailsViewModel(
            getProductUseCase = GetProductUseCase(testProductRepository),
            observeCartItemByProductIdUseCase = ObserveCartItemByProductIdUseCase(testCartRepository),
            addCartItemUseCase = AddCartItemUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            productId = testProductId
        )

        // Assert
        assertThat(viewModel.uiState.value.quantity).isEqualTo(3)
        assertThat(viewModel.uiState.value.cartItemId).isNotNull()
    }

    @Test
    fun `onAddToCart should add item to cart`() = runTest {
        // Arrange
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.getProductResult = Resource.Success(createTestProduct())

        val viewModel = ProductDetailsViewModel(
            getProductUseCase = GetProductUseCase(testProductRepository),
            observeCartItemByProductIdUseCase = ObserveCartItemByProductIdUseCase(testCartRepository),
            addCartItemUseCase = AddCartItemUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            productId = testProductId
        )

        // Act
        viewModel.onAddToCart()

        // Assert
        assertThat(viewModel.uiState.value.quantity).isEqualTo(1)
        assertThat(viewModel.uiState.value.cartItemId).isNotNull()
    }

    @Test
    fun `onIncrementQuantity should increase quantity by one`() = runTest {
        // Arrange - add cart item first
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.getProductResult = Resource.Success(createTestProduct())

        testCartRepository.addCartItem(
            CartItemEntity(
                id = 0,
                productId = testProductId,
                productName = "Test Product",
                productImageUrl = "test-url",
                unitPrice = 99.99,
                quantity = 2
            )
        )

        val viewModel = ProductDetailsViewModel(
            getProductUseCase = GetProductUseCase(testProductRepository),
            observeCartItemByProductIdUseCase = ObserveCartItemByProductIdUseCase(testCartRepository),
            addCartItemUseCase = AddCartItemUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            productId = testProductId
        )

        // Act
        viewModel.onIncrementQuantity()

        // Assert
        assertThat(viewModel.uiState.value.quantity).isEqualTo(3)
    }

    @Test
    fun `onDecrementQuantity should decrease quantity when above 1`() = runTest {
        // Arrange - add cart item with quantity 2
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.getProductResult = Resource.Success(createTestProduct())

        testCartRepository.addCartItem(
            CartItemEntity(
                id = 0,
                productId = testProductId,
                productName = "Test Product",
                productImageUrl = "test-url",
                unitPrice = 99.99,
                quantity = 2
            )
        )

        val viewModel = ProductDetailsViewModel(
            getProductUseCase = GetProductUseCase(testProductRepository),
            observeCartItemByProductIdUseCase = ObserveCartItemByProductIdUseCase(testCartRepository),
            addCartItemUseCase = AddCartItemUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            productId = testProductId
        )

        // Act
        viewModel.onDecrementQuantity()

        // Assert
        assertThat(viewModel.uiState.value.quantity).isEqualTo(1)
    }

    @Test
    fun `onDecrementQuantity should delete cart item when quantity is 1`() = runTest {
        // Arrange - add cart item with quantity 1
        val testProductRepository = TestProductRepository()
        val testCartRepository = TestCartRepository()
        testProductRepository.getProductResult = Resource.Success(createTestProduct())

        testCartRepository.addCartItem(
            CartItemEntity(
                id = 0,
                productId = testProductId,
                productName = "Test Product",
                productImageUrl = "test-url",
                unitPrice = 99.99,
                quantity = 1
            )
        )

        val viewModel = ProductDetailsViewModel(
            getProductUseCase = GetProductUseCase(testProductRepository),
            observeCartItemByProductIdUseCase = ObserveCartItemByProductIdUseCase(testCartRepository),
            addCartItemUseCase = AddCartItemUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            productId = testProductId
        )

        // Act
        viewModel.onDecrementQuantity()

        // Assert - item should be removed
        assertThat(viewModel.uiState.value.quantity).isEqualTo(0)
        assertThat(viewModel.uiState.value.cartItemId).isNull()
    }
}
