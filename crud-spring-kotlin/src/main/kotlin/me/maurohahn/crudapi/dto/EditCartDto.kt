package me.maurohahn.crudapi.dto

import javax.validation.constraints.NotNull

data class EditCartDto(

    @NotNull(message = "customerEncryptedId cannot be null")
    var customerEncryptedId: String,

    var productEncryptedIds: List<String> = listOf()

)