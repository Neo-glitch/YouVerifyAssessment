package org.neo.yvstore.features.order.presentation.screen.checkout

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.usecase.GetAddressByIdUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteAllCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.GetCartItemsUseCase
import org.neo.yvstore.features.order.domain.usecase.PlaceOrderUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestAddressRepository
import org.neo.yvstore.testdoubles.repository.TestCartRepository
import org.neo.yvstore.testdoubles.repository.TestOrderRepository

class CheckoutViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testAddressId = "a1"

    private fun createTestAddress() = Address(
        id = testAddressId,
        userId = "u1",
        streetAddress = "123 Main St",
        city = "New York",
        state = "NY",
        country = "USA"
    )

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
    fun `init should load cart items and address`() = runTest {
        // Arrange - set up data BEFORE constructing ViewModel
        val testCartRepository = TestCartRepository()
        val testAddressRepository = TestAddressRepository()
        val testOrderRepository = TestOrderRepository()

        createTestCartItems().forEach { testCartRepository.addCartItem(it) }
        testAddressRepository.emit(listOf(createTestAddress()))

        // Act - construct ViewModel (init{} runs here)
        val viewModel = CheckoutViewModel(
            getCartItemsUseCase = GetCartItemsUseCase(testCartRepository),
            getAddressByIdUseCase = GetAddressByIdUseCase(testAddressRepository),
            placeOrderUseCase = PlaceOrderUseCase(testOrderRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository),
            addressId = testAddressId
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(CheckoutLoadState.Loaded)
        assertThat(state.cartItems).hasSize(2)
        assertThat(state.address).isNotNull()
        assertThat(state.address?.streetAddress).isEqualTo("123 Main St")
    }

    @Test
    fun `placeOrder should clear cart and emit OrderPlaced on success`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
        val testAddressRepository = TestAddressRepository()
        val testOrderRepository = TestOrderRepository()

        createTestCartItems().forEach { testCartRepository.addCartItem(it) }
        testAddressRepository.emit(listOf(createTestAddress()))
        testOrderRepository.placeOrderResult = Resource.Success("order-123")

        val viewModel = CheckoutViewModel(
            getCartItemsUseCase = GetCartItemsUseCase(testCartRepository),
            getAddressByIdUseCase = GetAddressByIdUseCase(testAddressRepository),
            placeOrderUseCase = PlaceOrderUseCase(testOrderRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository),
            addressId = testAddressId
        )

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.placeOrder()

            val event = awaitItem()
            assertThat(event).isEqualTo(CheckoutUiEvent.OrderPlaced)

            cancelAndConsumeRemainingEvents()
        }

        // Verify cart was cleared
        val cartResult = testCartRepository.getCartItems()
        assertThat(cartResult).isInstanceOf(Resource.Success::class.java)
        assertThat((cartResult as Resource.Success).data).isEmpty()
    }

    @Test
    fun `placeOrder should set Error state on failure`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
        val testAddressRepository = TestAddressRepository()
        val testOrderRepository = TestOrderRepository()

        createTestCartItems().forEach { testCartRepository.addCartItem(it) }
        testAddressRepository.emit(listOf(createTestAddress()))
        testOrderRepository.placeOrderResult = Resource.Error("Payment failed")

        val viewModel = CheckoutViewModel(
            getCartItemsUseCase = GetCartItemsUseCase(testCartRepository),
            getAddressByIdUseCase = GetAddressByIdUseCase(testAddressRepository),
            placeOrderUseCase = PlaceOrderUseCase(testOrderRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository),
            addressId = testAddressId
        )

        // Act
        viewModel.placeOrder()

        // Assert
        val placeOrderState = viewModel.uiState.value.placeOrderState
        assertThat(placeOrderState).isInstanceOf(PlaceOrderState.Error::class.java)
        assertThat((placeOrderState as PlaceOrderState.Error).message).isEqualTo("Payment failed")
    }

    @Test
    fun `placeOrder should not proceed when address is null`() = runTest {
        // Arrange - don't add address to repository
        val testCartRepository = TestCartRepository()
        val testAddressRepository = TestAddressRepository()
        val testOrderRepository = TestOrderRepository()

        createTestCartItems().forEach { testCartRepository.addCartItem(it) }

        val viewModel = CheckoutViewModel(
            getCartItemsUseCase = GetCartItemsUseCase(testCartRepository),
            getAddressByIdUseCase = GetAddressByIdUseCase(testAddressRepository),
            placeOrderUseCase = PlaceOrderUseCase(testOrderRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository),
            addressId = testAddressId
        )

        val initialState = viewModel.uiState.value.placeOrderState

        // Act
        viewModel.placeOrder()

        // Assert - state should not change
        assertThat(viewModel.uiState.value.placeOrderState).isEqualTo(initialState)
        assertThat(viewModel.uiState.value.placeOrderState).isEqualTo(PlaceOrderState.Idle)
    }

    @Test
    fun `dismissError should reset placeOrderState to Idle`() = runTest {
        // Arrange
        val testCartRepository = TestCartRepository()
        val testAddressRepository = TestAddressRepository()
        val testOrderRepository = TestOrderRepository()

        createTestCartItems().forEach { testCartRepository.addCartItem(it) }
        testAddressRepository.emit(listOf(createTestAddress()))
        testOrderRepository.placeOrderResult = Resource.Error("Test error")

        val viewModel = CheckoutViewModel(
            getCartItemsUseCase = GetCartItemsUseCase(testCartRepository),
            getAddressByIdUseCase = GetAddressByIdUseCase(testAddressRepository),
            placeOrderUseCase = PlaceOrderUseCase(testOrderRepository),
            deleteAllCartItemsUseCase = DeleteAllCartItemsUseCase(testCartRepository),
            addressId = testAddressId
        )

        viewModel.placeOrder()

        // Act
        viewModel.dismissError()

        // Assert
        assertThat(viewModel.uiState.value.placeOrderState).isEqualTo(PlaceOrderState.Idle)
    }
}
