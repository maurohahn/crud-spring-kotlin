package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.Order

data class OrderDto(

    var encryptedId: String? = null,

    var customerEncryptedId: String? = null,

    var items: List<OrderItemDto> = listOf(),

    var totalPrice: Float? = null

) {

    constructor(data: Order) : this() {
        encryptedId = data.encryptedId
        customerEncryptedId = data.customer?.encryptedId
        items = data.items.map { OrderItemDto(it) }
        totalPrice = data.totalPrice
    }

}
