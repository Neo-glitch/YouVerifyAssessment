package org.neo.yvstore.features.cart.presentation.model

import org.neo.yvstore.core.domain.model.CartItem

data class CartItemUi(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int,
) {
    val formattedPrice: String
        get() = "$%.2f".format(price)

    val itemTotal: Double
        get() = price * quantity

    val formattedItemTotal: String
        get() = "$%.2f".format(itemTotal)
}

fun CartItem.toCartItemUi(): CartItemUi {
    return CartItemUi(
        id = id,
        name = productName,
        price = unitPrice,
        imageUrl = productImageUrl,
        quantity = quantity,
    )
}
