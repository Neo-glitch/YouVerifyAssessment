package org.neo.yvstore.features.cart.domain.repository

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource

interface CartRepository {
    fun observeCartItems(): Flow<Resource<List<CartItem>>>
    suspend fun getCartItems(): Resource<List<CartItem>>
    fun observeCartItemCount(): Flow<Resource<Int>>
    suspend fun addCartItem(cartItem: CartItemEntity): Resource<Unit>
    suspend fun updateQuantity(id: Long, quantity: Int): Resource<Unit>
    suspend fun deleteCartItem(id: Long): Resource<Unit>
    suspend fun deleteAllCartItems(): Resource<Unit>
    fun observeCartItemByProductId(productId: String): Flow<Resource<CartItem?>>
}
