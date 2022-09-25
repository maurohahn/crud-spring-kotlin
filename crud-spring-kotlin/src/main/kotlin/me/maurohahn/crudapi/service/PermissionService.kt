package me.maurohahn.crudapi.service

import me.maurohahn.crudapi.dto.EditPermissionDto
import me.maurohahn.crudapi.entity.Permission
import me.maurohahn.crudapi.exception.ForbiddenAccessIsDefaultRecord
import me.maurohahn.crudapi.exception.NotFoundException
import me.maurohahn.crudapi.repository.PermissionRepository
import org.springframework.stereotype.Service

@Service
class PermissionService(
    val permissionRepository: PermissionRepository
) {

    fun findOne(id: Long): Permission {
        return permissionRepository.findById(id).orElseThrow { NotFoundException() }
    }

    fun findMany(ids: List<Long>): List<Permission> {
        val permissionList = permissionRepository.findAllById(ids)
        val idsFound = permissionList.map { it.id }

        ids.forEach {
            val wasFound = idsFound.contains(it)

            if (!wasFound) {
                throw NotFoundException()
            }
        }

        return permissionList
    }

    fun findAll(): List<Permission> {
        return permissionRepository.findAll()
    }

    fun create(data: EditPermissionDto): Permission {
        val newPermission = Permission().apply {
            this.permissionName = data.permissionName
            this.isActive = data.isActive
        }

        return permissionRepository.save(newPermission)
    }

    fun createInBatch(dataList: List<EditPermissionDto>): List<Permission> {
        val newPermissionList = mutableListOf<Permission>()

        dataList.forEach { data ->
            val newPermission = Permission().apply {
                this.permissionName = data.permissionName
                this.isActive = data.isActive
            }

            newPermissionList.add(newPermission)
        }

        return permissionRepository.saveAll(newPermissionList)
    }

    fun update(id: Long, data: EditPermissionDto): Permission {
        val permissionFound = findOne(id)

        if (permissionFound.isDefault) {
            if (permissionFound.permissionName != data.permissionName) {
                throw ForbiddenAccessIsDefaultRecord()
            }
        }

        permissionFound.apply {
            this.permissionName = data.permissionName
            this.isActive = data.isActive
        }

        return permissionRepository.save(permissionFound)
    }

    fun delete(id: Long) {

        val permissionFound = findOne(id) // validate

        if (permissionFound.isDefault) {
            throw ForbiddenAccessIsDefaultRecord()
        }

        permissionRepository.deleteById(id)
    }
}