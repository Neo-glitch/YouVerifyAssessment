package org.neo.yvstore.features.cart.data.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.core.database.model.CartItemEntity

class CartItemMapperTest {

    @Test
    fun `toCartItem should map all CartItemEntity fields to CartItem`() {
        val entity = CartItemEntity(
            id = 1L,
            productId = "p1",
            productName = "Phone",
            productImageUrl = "https://img.com/phone.png",
            unitPrice = 999.99,
            quantity = 2
        )

        val cartItem = entity.toCartItem()

        assertThat(cartItem.id).isEqualTo(1L)
        assertThat(cartItem.productId).isEqualTo("p1")
        assertThat(cartItem.productName).isEqualTo("Phone")
        assertThat(cartItem.productImageUrl).isEqualTo("https://img.com/phone.png")
        assertThat(cartItem.unitPrice).isEqualTo(999.99)
        assertThat(cartItem.quantity).isEqualTo(2)
    }
}
