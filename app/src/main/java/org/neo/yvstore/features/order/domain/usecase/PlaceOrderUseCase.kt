package org.neo.yvstore.features.order.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.order.domain.repository.OrderRepository

class PlaceOrderUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        totalAmount: Double,
        shippingAddress: String,
        cartItemIds: List<String>
    ): Resource<String> = repository.placeOrder(totalAmount, shippingAddress, cartItemIds)
}
