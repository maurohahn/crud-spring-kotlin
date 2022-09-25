package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.User

data class UserDto(

    var encryptedId: String? = null,

    var name: String? = null,

    var email: String? = null,

    var isActive: Boolean? = null,

    var groupEncryptedIds: List<String> = listOf()

) {

    constructor(data: User) : this() {
        encryptedId = data.encryptedId
        name = data.name
        email = data.email
        isActive = data.isActive
        groupEncryptedIds = data.groups.map { it.encryptedId.toString() }
    }

}