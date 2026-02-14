package org.neo.yvstore.features.product.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val createdAt: String
)
