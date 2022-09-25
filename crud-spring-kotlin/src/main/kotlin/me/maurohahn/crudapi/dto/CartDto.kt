package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.Cart

data class CartDto(

    var encryptedId: String? = null,

    var customerEncryptedId: String? = null,

    var productEncryptedIds: List<String> = listOf()

) {

    constructor(data: Cart) : this() {
        encryptedId = data.encryptedId
        customerEncryptedId = data.customer?.encryptedId
        productEncryptedIds = data.products.map { it.encryptedId.toString() }

    }

}
