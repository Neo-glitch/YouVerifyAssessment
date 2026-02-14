package org.neo.yvstore.features.cart.domain.repository

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource

interface CartRepository {
    fun getCartItems(): Flow<Resource<List<CartItem>>>
    suspend fun addItem(cartItem: CartItemEntity): Resource<Unit>
    suspend fun updateQuantity(id: Long, quantity: Int): Resource<Unit>
    suspend fun deleteItem(id: Long): Resource<Unit>
    suspend fun deleteAllItems(): Resource<Unit>
}
