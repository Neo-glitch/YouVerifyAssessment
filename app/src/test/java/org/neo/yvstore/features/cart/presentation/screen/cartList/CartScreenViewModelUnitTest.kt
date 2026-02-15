package org.neo.yvstore.features.cart.presentation.screen.cartList

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.usecase.DeleteAllCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.UpdateCartItemQuantityUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
class CartScreenViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeCartItemsUseCase: ObserveCartItemsUseCase = mockk()
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase = mockk()
    private val deleteCartItemUseCase: DeleteCartItemUseCase = mockk()
    private val deleteAllCartItemsUseCase: DeleteAllCartItemsUseCase = mockk()

    private val cartItem = CartItem(1L, "p1", "Shoe",
        "url", 99.0, 2)

    private fun createViewModel(): CartScreenViewModel {
        return CartScreenViewModel(
            observeCartItemsUseCase,
            updateCartItemQuantityUseCase,
            deleteCartItemUseCase,
            deleteAllCartItemsUseCase
        )
    }

    @Test
    fun `init observes cart items and sets loaded state`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(listOf(cartItem)))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.cartItems).hasSize(1)
        assertThat(state.cartItems[0].name).isEqualTo("Shoe")
        assertThat(state.loadState).isEqualTo(CartScreenLoadState.Loaded)
    }

    @Test
    fun `init with error sets error state`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Error("Database error"))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.loadState).isInstanceOf(CartScreenLoadState.Error::class.java)
    }

    @Test
    fun `onIncrementQuantity calls update with incremented value`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(listOf(cartItem)))
        coEvery { updateCartItemQuantityUseCase(1L, 3) } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIncrementQuantity(1L)
        advanceUntilIdle()

        coVerify { updateCartItemQuantityUseCase(1L, 3) }
    }

    @Test
    fun `onDecrementQuantity with quantity gt 1 decrements`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(listOf(cartItem)))
        coEvery { updateCartItemQuantityUseCase(1L, 1) } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onDecrementQuantity(1L)
        advanceUntilIdle()

        coVerify { updateCartItemQuantityUseCase(1L, 1) }
    }

    @Test
    fun `onDecrementQuantity with quantity 1 does nothing`() = runTest {
        val itemWithQty1 = cartItem.copy(quantity = 1)
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(listOf(itemWithQty1)))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onDecrementQuantity(1L)
        advanceUntilIdle()

        coVerify(exactly = 0) { updateCartItemQuantityUseCase(any(), any()) }
    }

    @Test
    fun `onRemoveItem calls delete use case`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(listOf(cartItem)))
        coEvery { deleteCartItemUseCase(1L) } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onRemoveItem(1L)
        advanceUntilIdle()

        coVerify { deleteCartItemUseCase(1L) }
    }

    @Test
    fun `onShowClearCartDialog sets flag to true`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(emptyList()))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onShowClearCartDialog()

        assertThat(viewModel.uiState.value.showClearCartDialog).isTrue()
    }

    @Test
    fun `onDismissClearCartDialog sets flag to false`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(emptyList()))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onShowClearCartDialog()
        viewModel.onDismissClearCartDialog()

        assertThat(viewModel.uiState.value.showClearCartDialog).isFalse()
    }

    @Test
    fun `onConfirmClearCart calls deleteAll and hides dialog`() = runTest {
        every { observeCartItemsUseCase() } returns flowOf(Resource.Success(listOf(cartItem)))
        coEvery { deleteAllCartItemsUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onShowClearCartDialog()
        viewModel.onConfirmClearCart()
        advanceUntilIdle()

        coVerify { deleteAllCartItemsUseCase() }
        assertThat(viewModel.uiState.value.showClearCartDialog).isFalse()
    }
}
