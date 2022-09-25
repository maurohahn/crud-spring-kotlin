package me.maurohahn.crudapi.dto

data class EditUserDto(

    var name: String? = null,

    var password: String? = null,

    var email: String? = null,

    var isActive: Boolean? = null,

    var groupEncryptedIds: List<String> = listOf()

)