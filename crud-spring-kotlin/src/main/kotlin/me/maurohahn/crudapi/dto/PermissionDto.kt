package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.Permission

data class PermissionDto(

    var encryptedId: String? = null,

    var permissionName: String? = null,

    var isActive: Boolean? = null,

    ) {

    constructor(data: Permission) : this() {

        this.encryptedId = data.encryptedId
        this.permissionName = data.permissionName
        this.isActive = data.isActive

    }

}