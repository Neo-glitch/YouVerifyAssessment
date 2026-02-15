package org.neo.yvstore.features.product.data.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.core.database.model.ProductEntity
import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto

class ProductMappersTest {

    @Test
    fun `toEntity should map all ProductDto fields to ProductEntity`() {
        val dto = ProductDto(
            id = "p1",
            name = "Phone",
            description = "A smartphone",
            price = 999.99,
            imageUrl = "https://img.com/phone.png",
            rating = 4.5,
            reviewCount = 120,
            createdAt = "2024-01-01",
            searchName = "phone"
        )

        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo("p1")
        assertThat(entity.name).isEqualTo("Phone")
        assertThat(entity.description).isEqualTo("A smartphone")
        assertThat(entity.price).isEqualTo(999.99)
        assertThat(entity.imageUrl).isEqualTo("https://img.com/phone.png")
        assertThat(entity.rating).isEqualTo(4.5)
        assertThat(entity.reviewCount).isEqualTo(120)
        assertThat(entity.createdAt).isEqualTo("2024-01-01")
    }

    @Test
    fun `toProduct should map all ProductEntity fields to Product`() {
        val entity = ProductEntity(
            id = "p1",
            name = "Phone",
            description = "A smartphone",
            price = 999.99,
            imageUrl = "https://img.com/phone.png",
            rating = 4.5,
            reviewCount = 120,
            createdAt = "2024-01-01"
        )

        val product = entity.toProduct()

        assertThat(product.id).isEqualTo("p1")
        assertThat(product.name).isEqualTo("Phone")
        assertThat(product.description).isEqualTo("A smartphone")
        assertThat(product.price).isEqualTo(999.99)
        assertThat(product.imageUrl).isEqualTo("https://img.com/phone.png")
        assertThat(product.rating).isEqualTo(4.5)
        assertThat(product.reviewCount).isEqualTo(120)
        assertThat(product.createdAt).isEqualTo("2024-01-01")
    }

    @Test
    fun `toProduct should map ProductDto directly to Product`() {
        val dto = ProductDto(
            id = "p2",
            name = "Tablet",
            description = "A tablet",
            price = 499.99,
            imageUrl = "https://img.com/tablet.png",
            rating = 4.0,
            reviewCount = 50,
            createdAt = "2024-02-01",
            searchName = "tablet"
        )

        val product = dto.toProduct()

        assertThat(product.id).isEqualTo("p2")
        assertThat(product.name).isEqualTo("Tablet")
        assertThat(product.price).isEqualTo(499.99)
        assertThat(product.imageUrl).isEqualTo("https://img.com/tablet.png")
    }
}
