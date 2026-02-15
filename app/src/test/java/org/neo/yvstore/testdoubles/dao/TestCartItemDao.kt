package org.neo.yvstore.testdoubles.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.database.dao.CartItemDao
import org.neo.yvstore.core.database.model.CartItemEntity

class TestCartItemDao : CartItemDao {
    private val cartItems = mutableListOf<CartItemEntity>()
    private val cartItemsFlow = MutableSharedFlow<List<CartItemEntity>>(replay = 1)
    private var nextId = 1L

    init {
        cartItemsFlow.tryEmit(emptyList())
    }

    override fun observeAllCartItems(): Flow<List<CartItemEntity>> = cartItemsFlow

    override suspend fun getCartItems(): List<CartItemEntity> = cartItems.toList()

    override fun observeCartItemCount(): Flow<Int> = cartItemsFlow.map { it.size }

    override suspend fun insertCartItem(cartItem: CartItemEntity) {
        val itemToInsert = if (cartItem.id == 0L) {
            cartItem.copy(id = nextId++)
        } else {
            cartItems.removeIf { it.id == cartItem.id }
            cartItem
        }
        cartItems.add(itemToInsert)
        cartItems.sortBy { it.id }
        cartItemsFlow.emit(cartItems.toList())
    }

    override suspend fun updateQuantity(id: Long, quantity: Int) {
        val index = cartItems.indexOfFirst { it.id == id }
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = quantity)
            cartItemsFlow.emit(cartItems.toList())
        }
    }

    override suspend fun deleteCartItem(id: Long) {
        cartItems.removeIf { it.id == id }
        cartItemsFlow.emit(cartItems.toList())
    }

    override suspend fun deleteAllCartItems() {
        cartItems.clear()
        cartItemsFlow.emit(emptyList())
    }

    override fun observeCartItemByProductId(productId: String): Flow<CartItemEntity?> {
        return cartItemsFlow.map { items -> items.find { it.productId == productId } }
    }
}
