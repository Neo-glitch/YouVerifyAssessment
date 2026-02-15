package org.neo.yvstore.features.order.domain.repository

import org.neo.yvstore.core.domain.model.Resource

interface OrderRepository {
    suspend fun placeOrder(
        totalAmount: Double,
        shippingAddress: String,
        cartItemIds: List<String>
    ): Resource<String>
}
