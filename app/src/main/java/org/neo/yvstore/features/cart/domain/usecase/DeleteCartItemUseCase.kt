package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

/**
 * Use case for deleting a cart item.
 *
 * @property repository The cart repository for accessing cart data
 */
class DeleteCartItemUseCase(
    private val repository: CartRepository
) {
    /**
     * Deletes a cart item by ID.
     *
     * @param id The cart item ID to delete
     * @return Resource indicating success or failure
     */
    suspend operator fun invoke(id: Long): Resource<Unit> =
        repository.deleteItem(id)
}
