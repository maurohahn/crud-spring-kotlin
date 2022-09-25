package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.Group

data class GroupDto(

    var encryptedId: String? = null,

    var groupName: String? = null,

    var isActive: Boolean? = null,

    var permissionEncryptedIds: List<String> = listOf()

) {

    constructor(data: Group) : this() {
        encryptedId = data.encryptedId
        groupName = data.groupName
        isActive = data.isActive
        permissionEncryptedIds = data.permissions.map { it.encryptedId.toString() }
    }

}