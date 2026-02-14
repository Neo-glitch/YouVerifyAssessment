package org.neo.yvstore.core.domain.model

data class CartItem(
    val id: Long,
    val productId: String,
    val productName: String,
    val productImageUrl: String,
    val unitPrice: Double,
    val quantity: Int
)
