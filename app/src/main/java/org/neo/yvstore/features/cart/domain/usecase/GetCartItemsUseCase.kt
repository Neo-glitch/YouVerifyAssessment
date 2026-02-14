package org.neo.yvstore.features.cart.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

/**
 * Use case for observing cart items.
 * Returns a reactive Flow that emits whenever the cart items change.
 *
 * @property repository The cart repository for accessing cart data
 */
class GetCartItemsUseCase(
    private val repository: CartRepository
) {
    /**
     * Returns a Flow of cart items wrapped in Resource.
     *
     * @return Flow of Resource-wrapped cart items
     */
    operator fun invoke(): Flow<Resource<List<CartItem>>> = repository.getCartItems()
}
