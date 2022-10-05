package me.maurohahn.crudapi.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class EditOrderItemDto(

    @NotNull(message = "productEncryptedId cannot be null")
    var productEncryptedId: String,

    @Positive
    @NotNull(message = "quantity cannot be null")
    var quantity: Int
)
