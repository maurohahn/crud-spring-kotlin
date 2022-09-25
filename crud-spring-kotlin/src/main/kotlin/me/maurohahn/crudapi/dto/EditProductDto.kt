package me.maurohahn.crudapi.dto

data class EditProductDto(

    var description: String? = null,

    var price: Float? = null,

    var brand: String? = null,

    var quantity: Int? = null,

    var category: String? = null,

    var isActive: Boolean? = null,

)