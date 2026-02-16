package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class UpdateCartItemQuantityUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(id: Long, quantity: Int): Resource<Unit> =
        repository.updateQuantity(id, quantity)
}
