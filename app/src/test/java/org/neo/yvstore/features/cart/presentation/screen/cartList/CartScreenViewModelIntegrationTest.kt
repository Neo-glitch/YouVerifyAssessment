package org.neo.yvstore.features.cart.presentation.screen.cartList

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.features.cart.domain.usecase.DeleteAllCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.UpdateCartItemQuantityUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestCartRepository

class CartScreenViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createTestCartItems() = listOf(
        CartItemEntity(
            id = 0,
            productId = "p1",
            productName = "Product 1",
            productImageUrl = "url1",
            unitPrice = 10.0,
            quantity = 2
        ),
        CartItemEntity(
            id = 0,
            productId = "p2",
            productName = "Product 2",
            productImageUrl = "url2",
            unitPrice = 20.0,
            quantity = 1
        )
    )

    @Test
    fun `init should observe and display cart items`() = runTest {
        // Arrange - add items to repo BEFORE constructing ViewModel
        val testCartRepository = TestCartRepository()
        createTestCartItems().forEach { testCartRepository.addCartItem(it) }

        // Act - construct ViewModel (init{} runs here)
        val viewModel = CartScreenViewModel(
            observeCartItemsUseCase = ObserveCartItemsUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository)
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(CartScreenLoadState.Loaded)
        assertThat(state.cartItems).hasSize(2)
        assertThat(state.cartItems[0].name).isEqualTo("Product 1")
        assertThat(state.cartItems[0].quantity).isEqualTo(2)
        assertThat(state.cartItems[1].name).isEqualTo("Product 2")
        assertThat(state.cartItems[1].quantity).isEqualTo(1)
    }

    @Test
    fun `onIncrementQuantity should increase item quantity by one`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
        testCartRepository.addCartItem(
            CartItemEntity(
                id = 0,
                productId = "p1",
                productName = "Product 1",
                productImageUrl = "url1",
                unitPrice = 10.0,
                quantity = 2
            )
        )

        val viewModel = CartScreenViewModel(
            observeCartItemsUseCase = ObserveCartItemsUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository)
        )

        val itemId = viewModel.uiState.value.cartItems[0].id

        // Act
        viewModel.onIncrementQuantity(itemId)

        // Assert
        assertThat(viewModel.uiState.value.cartItems[0].quantity).isEqualTo(3)
    }

    @Test
    fun `onDecrementQuantity should decrease item quantity when above 1`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
        testCartRepository.addCartItem(
            CartItemEntity(
                id = 0,
                productId = "p1",
                productName = "Product 1",
                productImageUrl = "url1",
                unitPrice = 10.0,
                quantity = 3
            )
        )

        val viewModel = CartScreenViewModel(
            observeCartItemsUseCase = ObserveCartItemsUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository)
        )

        val itemId = viewModel.uiState.value.cartItems[0].id

        // Act
        viewModel.onDecrementQuantity(itemId)

        // Assert
        assertThat(viewModel.uiState.value.cartItems[0].quantity).isEqualTo(2)
    }

    @Test
    fun `onDecrementQuantity should not decrease when quantity is 1`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
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

        val viewModel = CartScreenViewModel(
            observeCartItemsUseCase = ObserveCartItemsUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository)
        )

        val itemId = viewModel.uiState.value.cartItems[0].id

        // Act
        viewModel.onDecrementQuantity(itemId)

        // Assert - quantity should remain 1
        assertThat(viewModel.uiState.value.cartItems[0].quantity).isEqualTo(1)
    }

    @Test
    fun `onRemoveItem should delete item from cart`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
        createTestCartItems().forEach { testCartRepository.addCartItem(it) }

        val viewModel = CartScreenViewModel(
            observeCartItemsUseCase = ObserveCartItemsUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository)
        )

        val itemId = viewModel.uiState.value.cartItems[0].id

        // Act
        viewModel.onRemoveItem(itemId)

        // Assert - item should be removed
        assertThat(viewModel.uiState.value.cartItems).hasSize(1)
        assertThat(viewModel.uiState.value.cartItems[0].name).isEqualTo("Product 2")
    }

    @Test
    fun `onConfirmClearCart should delete all items and dismiss dialog`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
        createTestCartItems().forEach { testCartRepository.addCartItem(it) }

        val viewModel = CartScreenViewModel(
            observeCartItemsUseCase = ObserveCartItemsUseCase(testCartRepository),
            updateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(testCartRepository),
            deleteCartItemUseCase = DeleteCartItemUseCase(testCartRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository)
        )

        // Show dialog first
        viewModel.onShowClearCartDialog()
        assertThat(viewModel.uiState.value.showClearCartDialog).isTrue()

        // Act
        viewModel.onConfirmClearCart()

        // Assert - all items should be removed and dialog dismissed
        assertThat(viewModel.uiState.value.cartItems).isEmpty()
        assertThat(viewModel.uiState.value.showClearCartDialog).isFalse()
    }
}
