package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class DeleteCartItemUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(id: Long): Resource<Unit> =
        repository.deleteCartItem(id)
}
