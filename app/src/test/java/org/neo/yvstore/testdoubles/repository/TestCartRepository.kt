package org.neo.yvstore.testdoubles.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class TestCartRepository : CartRepository {
    private val cartItems = mutableListOf<CartItem>()
    private val cartItemsFlow = MutableSharedFlow<List<CartItem>>(replay = 1)
    private var nextId = 1L

    init {
        cartItemsFlow.tryEmit(emptyList())
    }

    override fun observeCartItems(): Flow<Resource<List<CartItem>>> {
        return cartItemsFlow.map { Resource.Success(it) }
    }

    override suspend fun getCartItems(): Resource<List<CartItem>> {
        return Resource.Success(cartItems.toList())
    }

    override fun observeCartItemCount(): Flow<Resource<Int>> {
        return cartItemsFlow.map { Resource.Success(it.size) }
    }

    override suspend fun addCartItem(cartItem: CartItemEntity): Resource<Unit> {
        val id = if (cartItem.id == 0L) nextId++ else cartItem.id
        val item = CartItem(
            id = id,
            productId = cartItem.productId,
            productName = cartItem.productName,
            productImageUrl = cartItem.productImageUrl,
            unitPrice = cartItem.unitPrice,
            quantity = cartItem.quantity
        )
        cartItems.add(item)
        cartItemsFlow.emit(cartItems.toList())
        return Resource.Success(Unit)
    }

    override suspend fun updateQuantity(id: Long, quantity: Int): Resource<Unit> {
        val index = cartItems.indexOfFirst { it.id == id }
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = quantity)
            cartItemsFlow.emit(cartItems.toList())
            return Resource.Success(Unit)
        }
        return Resource.Error("Cart item not found")
    }

    override suspend fun deleteCartItem(id: Long): Resource<Unit> {
        cartItems.removeIf { it.id == id }
        cartItemsFlow.emit(cartItems.toList())
        return Resource.Success(Unit)
    }

    override suspend fun deleteAllCartItems(): Resource<Unit> {
        cartItems.clear()
        cartItemsFlow.emit(emptyList())
        return Resource.Success(Unit)
    }

    override fun observeCartItemByProductId(productId: String): Flow<Resource<CartItem?>> {
        return cartItemsFlow.map { items ->
            Resource.Success(items.find { it.productId == productId })
        }
    }
}
