package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.OrderItem

data class OrderItemDto(

    var encryptedId: String? = null,

    var product: ProductDto? = null,

    var quantity: Int? = null
) {

    constructor(data: OrderItem) : this() {
        encryptedId = data.encryptedId
        product = data.product?.let { ProductDto(it) }
        quantity = data.quantity
    }

}
