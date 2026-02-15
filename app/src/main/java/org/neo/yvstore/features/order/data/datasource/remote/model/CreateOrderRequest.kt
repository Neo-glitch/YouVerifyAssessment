package org.neo.yvstore.features.order.data.datasource.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class CreateOrderRequest(
    val id: String = "",
    @get:PropertyName("user_id")
    val userId: String = "",
    @get:PropertyName("total_amount")
    val totalAmount: Double = 0.0,
    @get:PropertyName("shipping_address")
    val shippingAddress: String = "",
    val status: String = "",
    @get:PropertyName("created_at")
    val createdAt: Timestamp = Timestamp.now(),
    @get:PropertyName("updated_at")
    val updatedAt: Timestamp = Timestamp.now(),
    @get:PropertyName("cart_item_id")
    val cartItemId: List<String> = emptyList()
)
