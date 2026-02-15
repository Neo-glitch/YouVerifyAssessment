package org.neo.yvstore.features.order.data.datasource.remote.model

import com.google.firebase.firestore.PropertyName

data class OrderItemRequest(
    @get:PropertyName("product_id")
    val productId: String = "",
    @get:PropertyName("product_name")
    val productName: String = "",
    @get:PropertyName("unit_price")
    val unitPrice: Double = 0.0,
    val quantity: Int = 0
)
