package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class AddCartItemUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartItem: CartItemEntity): Resource<Unit> =
        repository.addCartItem(cartItem)
}
