package org.neo.yvstore.features.cart.presentation.model

data class CartItemUi(
    val id: String,
    val name: String,
    val variantLabel: String,
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
