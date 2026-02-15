package org.neo.yvstore.features.product.presentation.model

import org.neo.yvstore.core.common.util.formatAsPrice

data class ProductDetailsUi(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val details: String,
) {
    val formattedPrice: String
        get() = price.formatAsPrice()
}
