package org.neo.yvstore.testdoubles.repository

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.order.domain.model.OrderLineItem
import org.neo.yvstore.features.order.domain.repository.OrderRepository

class TestOrderRepository : OrderRepository {
    var placeOrderResult: Resource<String> = Resource.Success("test-order-id")

    override suspend fun placeOrder(
        totalAmount: Double,
        shippingAddress: String,
        items: List<OrderLineItem>
    ): Resource<String> = placeOrderResult
}
