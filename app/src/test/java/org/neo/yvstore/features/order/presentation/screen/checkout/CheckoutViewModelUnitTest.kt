package org.neo.yvstore.features.order.presentation.screen.checkout

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.usecase.GetAddressByIdUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteAllCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.GetCartItemsUseCase
import org.neo.yvstore.features.order.domain.usecase.PlaceOrderUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCartItemsUseCase: GetCartItemsUseCase = mockk()
    private val getAddressByIdUseCase: GetAddressByIdUseCase = mockk()
    private val placeOrderUseCase: PlaceOrderUseCase = mockk()
    private val deleteAllCartItemsUseCase: DeleteAllCartItemsUseCase = mockk()

    private val cartItem = CartItem(1L, "p1", "Shoe", "url", 99.0, 2)
    private val address = Address("a1", "u1", "123 Main", "City", "State", "Country")

    private fun createViewModel(): CheckoutViewModel {
        return CheckoutViewModel(
            getCartItemsUseCase,
            getAddressByIdUseCase,
            placeOrderUseCase,
            deleteAllCartItemsUseCase,
            addressId = "a1"
        )
    }

    @Test
    fun `init should load cart items and address`() = runTest {
        coEvery { getCartItemsUseCase() } returns Resource.Success(listOf(cartItem))
        coEvery { getAddressByIdUseCase("a1") } returns Resource.Success(address)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.cartItems).hasSize(1)
        assertThat(state.address).isNotNull()
        assertThat(state.loadState).isEqualTo(CheckoutLoadState.Loaded)
    }

    @Test
    fun `init with cart error and address error should set error state`() = runTest {
        coEvery { getCartItemsUseCase() } returns Resource.Error("Database error")
        coEvery { getAddressByIdUseCase("a1") } returns Resource.Error("Address not found")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.loadState).isInstanceOf(CheckoutLoadState.Error::class.java)
    }

    @Test
    fun `init with address error should set error state`() = runTest {
        coEvery { getCartItemsUseCase() } returns Resource.Success(listOf(cartItem))
        coEvery { getAddressByIdUseCase("a1") } returns Resource.Error("Address not found")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.loadState).isInstanceOf(CheckoutLoadState.Error::class.java)
    }

    @Test
    fun `placeOrder should clear cart and emit event on success`() = runTest {
        coEvery { getCartItemsUseCase() } returns Resource.Success(listOf(cartItem))
        coEvery { getAddressByIdUseCase("a1") } returns Resource.Success(address)
        coEvery { placeOrderUseCase(any(), any(), any()) } returns Resource.Success("order123")
        coEvery { deleteAllCartItemsUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiEvent.test {
            viewModel.placeOrder()
            advanceUntilIdle()
            val event = awaitItem()
            assertThat(event).isInstanceOf(CheckoutUiEvent.OrderPlaced::class.java)
        }

        coVerify { deleteAllCartItemsUseCase() }
    }

    @Test
    fun `placeOrder should update placeOrderState on error`() = runTest {
        coEvery { getCartItemsUseCase() } returns Resource.Success(listOf(cartItem))
        coEvery { getAddressByIdUseCase("a1") } returns Resource.Success(address)
        coEvery { placeOrderUseCase(any(), any(), any()) } returns Resource.Error("Network error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.placeOrder()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.placeOrderState).isInstanceOf(PlaceOrderState.Error::class.java)
    }

    @Test
    fun `dismissError should reset placeOrderState`() = runTest {
        coEvery { getCartItemsUseCase() } returns Resource.Success(listOf(cartItem))
        coEvery { getAddressByIdUseCase("a1") } returns Resource.Success(address)
        coEvery { placeOrderUseCase(any(), any(), any()) } returns Resource.Error("error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.placeOrder()
        advanceUntilIdle()
        viewModel.dismissError()

        assertThat(viewModel.uiState.value.placeOrderState).isEqualTo(PlaceOrderState.Idle)
    }
}
