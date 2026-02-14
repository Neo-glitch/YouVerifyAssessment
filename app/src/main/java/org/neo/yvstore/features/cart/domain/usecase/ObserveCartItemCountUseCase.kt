package org.neo.yvstore.features.cart.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

/**
 * Use case for observing the cart item count.
 * Returns a reactive Flow that emits whenever the cart items change.
 *
 * @property repository The cart repository for accessing cart data
 */
class ObserveCartItemCountUseCase(
    private val repository: CartRepository
) {
    /**
     * Returns a Flow of the cart item count wrapped in Resource.
     *
     * @return Flow of Resource-wrapped cart item count
     */
    operator fun invoke(): Flow<Resource<Int>> = repository.observeCartItemCount()
}
