package org.neo.yvstore.features.order.domain.repository

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.order.domain.model.OrderLineItem

interface OrderRepository {
    suspend fun placeOrder(
        totalAmount: Double,
        shippingAddress: String,
        items: List<OrderLineItem>
    ): Resource<String>
}
