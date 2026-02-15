package org.neo.yvstore.features.product.presentation.screen.productDetails

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.usecase.AddCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemByProductIdUseCase
import org.neo.yvstore.features.cart.domain.usecase.UpdateCartItemQuantityUseCase
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.GetProductUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProductUseCase: GetProductUseCase = mockk()
    private val observeCartItemByProductIdUseCase: ObserveCartItemByProductIdUseCase = mockk()
    private val addCartItemUseCase: AddCartItemUseCase = mockk()
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase = mockk()
    private val deleteCartItemUseCase: DeleteCartItemUseCase = mockk()

    private val product = Product("p1", "Shoe", "Desc", 99.0, "url", 4.5, 10, "2024-01-01")

    private fun createViewModel(): ProductDetailsViewModel {
        return ProductDetailsViewModel(
            getProductUseCase,
            observeCartItemByProductIdUseCase,
            addCartItemUseCase,
            updateCartItemQuantityUseCase,
            deleteCartItemUseCase,
            productId = "p1"
        )
    }

    @Test
    fun `init loads product and sets loaded state`() = runTest {
        coEvery { getProductUseCase("p1") } returns Resource.Success(product)
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(null))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.product?.name).isEqualTo("Shoe")
        assertThat(state.loadState).isEqualTo(ProductDetailsLoadState.Loaded)
    }

    @Test
    fun `init with product error sets error state`() = runTest {
        coEvery { getProductUseCase("p1") } returns Resource.Error("Product not found")
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(null))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(ProductDetailsLoadState.Error::class.java)
    }

    @Test
    fun `cart item observation updates quantity and cartItemId`() = runTest {
        val cartItem = CartItem(1L, "p1", "Shoe", "url", 99.0, 3)
        coEvery { getProductUseCase("p1") } returns Resource.Success(product)
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(cartItem))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.quantity).isEqualTo(3)
        assertThat(state.cartItemId).isEqualTo(1L)
    }

    @Test
    fun `onAddToCart calls addCartItemUseCase`() = runTest {
        coEvery { getProductUseCase("p1") } returns Resource.Success(product)
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(null))
        coEvery { addCartItemUseCase(any()) } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAddToCart()
        advanceUntilIdle()

        coVerify { addCartItemUseCase(match { it.productId == "p1" && it.quantity == 1 }) }
    }

    @Test
    fun `onIncrementQuantity updates quantity`() = runTest {
        val cartItem = CartItem(1L, "p1", "Shoe", "url", 99.0, 2)
        coEvery { getProductUseCase("p1") } returns Resource.Success(product)
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(cartItem))
        coEvery { updateCartItemQuantityUseCase(1L, 3) } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIncrementQuantity()
        advanceUntilIdle()

        coVerify { updateCartItemQuantityUseCase(1L, 3) }
    }

    @Test
    fun `onDecrementQuantity with quantity gt 1 decrements`() = runTest {
        val cartItem = CartItem(1L, "p1", "Shoe", "url", 99.0, 2)
        coEvery { getProductUseCase("p1") } returns Resource.Success(product)
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(cartItem))
        coEvery { updateCartItemQuantityUseCase(1L, 1) } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onDecrementQuantity()
        advanceUntilIdle()

        coVerify { updateCartItemQuantityUseCase(1L, 1) }
    }

    @Test
    fun `onDecrementQuantity with quantity 1 deletes item`() = runTest {
        val cartItem = CartItem(1L, "p1", "Shoe", "url", 99.0, 1)
        coEvery { getProductUseCase("p1") } returns Resource.Success(product)
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(cartItem))
        coEvery { deleteCartItemUseCase(1L) } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onDecrementQuantity()
        advanceUntilIdle()

        coVerify { deleteCartItemUseCase(1L) }
    }

    @Test
    fun `onAddToCart error emits error event`() = runTest {
        coEvery { getProductUseCase("p1") } returns Resource.Success(product)
        every { observeCartItemByProductIdUseCase("p1") } returns flowOf(Resource.Success(null))
        coEvery { addCartItemUseCase(any()) } returns Resource.Error("Database error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiEvent.test {
            viewModel.onAddToCart()
            val event = awaitItem()
            assertThat(event).isInstanceOf(ProductDetailsUiEvent.Error::class.java)
            assertThat((event as ProductDetailsUiEvent.Error).message).isEqualTo("Database error")
        }
    }
}
