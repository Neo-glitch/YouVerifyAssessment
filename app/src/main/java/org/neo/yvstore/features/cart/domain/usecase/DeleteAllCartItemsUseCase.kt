package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class DeleteAllCartItemsUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): Resource<Unit> =
        repository.deleteAllCartItems()
}
