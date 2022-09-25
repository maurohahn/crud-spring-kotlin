package me.maurohahn.crudapi.dto

data class EditGroupDto(

    var groupName: String? = null,

    var isActive: Boolean? = null,

    var permissionEncryptedIds: List<String> = listOf()

)