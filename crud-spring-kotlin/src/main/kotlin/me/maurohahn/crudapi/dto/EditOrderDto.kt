package me.maurohahn.crudapi.dto

import javax.validation.constraints.NotNull

data class EditOrderDto(

    @NotNull(message = "customerEncryptedId cannot be null")
    var customerEncryptedId: String,

    var items: List<EditOrderItemDto> = listOf()

)