package org.neo.yvstore.features.order.data.datasource.remote

import org.neo.yvstore.features.order.data.datasource.remote.model.CreateOrderRequest

interface OrderRemoteDatasource {
    suspend fun createOrder(request: CreateOrderRequest): String
}
