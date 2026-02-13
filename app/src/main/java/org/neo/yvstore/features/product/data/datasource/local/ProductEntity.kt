package org.neo.yvstore.features.product.data.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    val rating: Double,
    @ColumnInfo(name = "review_count")
    val reviewCount: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: String
)
