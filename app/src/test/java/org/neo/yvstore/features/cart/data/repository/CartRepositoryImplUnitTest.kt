package org.neo.yvstore.features.cart.data.repository

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.database.dao.CartItemDao
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Resource
import com.google.common.truth.Truth.assertThat

class CartRepositoryImplUnitTest {

    private val cartItemDao: CartItemDao = mockk()
    private lateinit var repository: CartRepositoryImpl

    private val entity = CartItemEntity(
        id = 1L, productId = "p1", productName = "Shoe",
        productImageUrl = "url", unitPrice = 99.0, quantity = 2
    )

    @Before
    fun setUp() {
        repository = CartRepositoryImpl(cartItemDao)
    }

    // ── observeCartItems ──

    @Test
    fun `observeCartItems emits success with mapped items`() = runTest {
        every { cartItemDao.observeAllCartItems() } returns flowOf(listOf(entity))

        repository.observeCartItems().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val items = (result as Resource.Success).data
            assertThat(items).hasSize(1)
            assertThat(items[0].id).isEqualTo(1L)
            assertThat(items[0].productName).isEqualTo("Shoe")
            awaitComplete()
        }
    }

    @Test
    fun `observeCartItems emits error when dao throws`() = runTest {
        every { cartItemDao.observeAllCartItems() } returns flow {
            throw RuntimeException("db")
        }

        repository.observeCartItems().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
            awaitComplete()
        }
    }

    // ── getCartItems ──

    @Test
    fun `getCartItems returns success with mapped items`() = runTest {
        coEvery { cartItemDao.getCartItems() } returns listOf(entity)

        val result = repository.getCartItems()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).hasSize(1)
    }

    @Test
    fun `getCartItems returns error when dao throws`() = runTest {
        coEvery { cartItemDao.getCartItems() } throws RuntimeException("db")

        val result = repository.getCartItems()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }

    // ── observeCartItemCount ──

    @Test
    fun `observeCartItemCount emits success with count`() = runTest {
        every { cartItemDao.observeCartItemCount() } returns flowOf(3)

        repository.observeCartItemCount().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat((result as Resource.Success).data).isEqualTo(3)
            awaitComplete()
        }
    }

    // ── addCartItem ──

    @Test
    fun `addCartItem returns success`() = runTest {
        coEvery { cartItemDao.insertCartItem(entity) } returns Unit

        val result = repository.addCartItem(entity)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { cartItemDao.insertCartItem(entity) }
    }

    @Test
    fun `addCartItem returns error when dao throws`() = runTest {
        coEvery { cartItemDao.insertCartItem(entity) } throws RuntimeException("db")

        val result = repository.addCartItem(entity)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }

    // ── updateQuantity ──

    @Test
    fun `updateQuantity returns success`() = runTest {
        coEvery { cartItemDao.updateQuantity(1L, 5) } returns Unit

        val result = repository.updateQuantity(1L, 5)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { cartItemDao.updateQuantity(1L, 5) }
    }

    // ── deleteCartItem ──

    @Test
    fun `deleteCartItem returns success`() = runTest {
        coEvery { cartItemDao.deleteCartItem(1L) } returns Unit

        val result = repository.deleteCartItem(1L)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { cartItemDao.deleteCartItem(1L) }
    }

    // ── deleteAllCartItems ──

    @Test
    fun `deleteAllCartItems returns success`() = runTest {
        coEvery { cartItemDao.deleteAllCartItems() } returns Unit

        val result = repository.deleteAllCartItems()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { cartItemDao.deleteAllCartItems() }
    }

    // ── observeCartItemByProductId ──

    @Test
    fun `observeCartItemByProductId emits success with mapped item`() = runTest {
        every { cartItemDao.observeCartItemByProductId("p1") } returns flowOf(entity)

        repository.observeCartItemByProductId("p1").test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat((result as Resource.Success).data?.id).isEqualTo(1L)
            awaitComplete()
        }
    }

    @Test
    fun `observeCartItemByProductId emits success with null when not found`() = runTest {
        every { cartItemDao.observeCartItemByProductId("p2") } returns flowOf(null)

        repository.observeCartItemByProductId("p2").test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat((result as Resource.Success).data).isNull()
            awaitComplete()
        }
    }
}
