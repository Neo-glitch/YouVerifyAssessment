package org.neo.yvstore.features.product.data.mapper

import org.neo.yvstore.core.database.model.ProductEntity
import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto
import org.neo.yvstore.features.product.domain.model.Product

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        rating = this.rating,
        reviewCount = this.reviewCount,
        createdAt = this.createdAt
    )
}

fun ProductEntity.toProduct(): Product {
    return Product(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        rating = this.rating,
        reviewCount = this.reviewCount,
        createdAt = this.createdAt
    )
}
