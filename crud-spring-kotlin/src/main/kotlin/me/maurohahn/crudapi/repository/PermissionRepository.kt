package me.maurohahn.crudapi.repository

import me.maurohahn.crudapi.entity.Permission
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepository : JpaRepository<Permission, Long> {

}