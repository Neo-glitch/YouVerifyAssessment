package org.neo.yvstore.features.cart.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class ObserveCartItemCountUseCase(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<Resource<Int>> = repository.observeCartItemCount()
}
