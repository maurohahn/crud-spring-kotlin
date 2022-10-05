package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.Product

data class ProductDto(

    var encryptedId: String? = null,

    var description: String? = null,

    var price: Float? = null,

    var brand: String? = null,

    var quantity: Int? = null,

    var category: String? = null,

    var isActive: Boolean? = null

) {

    constructor(data: Product) : this() {
        encryptedId = data.encryptedId
        description = data.description
        price = data.price
        brand = data.brand
        quantity = data.quantity
        category = data.category
        isActive = data.isActive
    }

}