package org.neo.yvstore.testdoubles.datasource

import org.neo.yvstore.features.order.data.datasource.remote.OrderRemoteDatasource
import org.neo.yvstore.features.order.data.datasource.remote.model.CreateOrderRequest

class TestOrderRemoteDatasource : OrderRemoteDatasource {
    var orderId: String = "test-order-id"
    var error: Exception? = null

    override suspend fun createOrder(request: CreateOrderRequest): String {
        error?.let { throw it }
        return orderId
    }
}
