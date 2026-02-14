package org.neo.yvstore.features.cart.domain.usecase

import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

/**
 * Use case for adding an item to the cart.
 *
 * @property repository The cart repository for accessing cart data
 */
class AddCartItemUseCase(
    private val repository: CartRepository
) {
    /**
     * Adds an item to the cart.
     *
     * @param cartItem The cart item entity to add
     * @return Resource indicating success or failure
     */
    suspend operator fun invoke(cartItem: CartItemEntity): Resource<Unit> =
        repository.addItem(cartItem)
}
