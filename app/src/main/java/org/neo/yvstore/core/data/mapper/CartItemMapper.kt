package org.neo.yvstore.core.data.mapper

import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.CartItem

fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        id = this.id,
        productId = this.productId,
        productName = this.productName,
        productImageUrl = this.productImageUrl,
        unitPrice = this.unitPrice,
        quantity = this.quantity
    )
}
