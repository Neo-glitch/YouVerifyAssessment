package org.neo.yvstore.features.cart.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

/**
 * Use case for observing a cart item by product ID.
 * Returns a reactive Flow that emits whenever the cart item changes.
 *
 * @property repository The cart repository for accessing cart data
 */
class ObserveCartItemByProductIdUseCase(
    private val repository: CartRepository
) {
    /**
     * Observes a cart item by product ID.
     * Emits null if no cart item exists for the given product ID.
     *
     * @param productId The product ID to observe
     * @return Flow of Resource-wrapped cart item (nullable)
     */
    operator fun invoke(productId: String): Flow<Resource<CartItem?>> =
        repository.observeCartItemByProductId(productId)
}
