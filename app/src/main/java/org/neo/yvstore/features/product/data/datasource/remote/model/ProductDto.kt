package org.neo.yvstore.features.product.data.datasource.remote.model

import com.google.firebase.firestore.PropertyName

/**
 * Remote data model representing a product from Firestore.
 * Default values are required for Firestore deserialization.
 */
data class ProductDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String = "",
    val rating: Double = 0.0,
    @get:PropertyName("review_count")
    @set:PropertyName("review_count")
    var reviewCount: Int = 0,
    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt: String = ""
)
