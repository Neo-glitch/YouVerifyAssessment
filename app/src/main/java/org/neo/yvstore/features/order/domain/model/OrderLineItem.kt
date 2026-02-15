package org.neo.yvstore.features.order.domain.model

data class OrderLineItem(
    val productId: String,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int
)
