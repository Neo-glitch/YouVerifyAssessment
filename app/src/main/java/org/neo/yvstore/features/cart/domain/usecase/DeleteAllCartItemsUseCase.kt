package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

/**
 * Use case for deleting all cart items.
 *
 * @property repository The cart repository for accessing cart data
 */
class DeleteAllCartItemsUseCase(
    private val repository: CartRepository
) {
    /**
     * Deletes all cart items.
     *
     * @return Resource indicating success or failure
     */
    suspend operator fun invoke(): Resource<Unit> =
        repository.deleteAllItems()
}
