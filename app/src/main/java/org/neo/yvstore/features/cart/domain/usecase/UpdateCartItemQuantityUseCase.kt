package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

/**
 * Use case for updating a cart item's quantity.
 *
 * @property repository The cart repository for accessing cart data
 */
class UpdateCartItemQuantityUseCase(
    private val repository: CartRepository
) {
    /**
     * Updates the quantity of a cart item.
     *
     * @param id The cart item ID
     * @param quantity The new quantity
     * @return Resource indicating success or failure
     */
    suspend operator fun invoke(id: Long, quantity: Int): Resource<Unit> =
        repository.updateQuantity(id, quantity)
}
