package me.maurohahn.crudapi.service

import me.maurohahn.crudapi.dto.EditGroupDto
import me.maurohahn.crudapi.entity.Group
import me.maurohahn.crudapi.exception.NotFoundException
import me.maurohahn.crudapi.repository.GroupRepository
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.stereotype.Service

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val permissionService: PermissionService
) {

    fun findOne(id: Long): Group {
        return groupRepository.findById(id).orElseThrow { NotFoundException() }
    }

    fun findMany(ids: List<Long>): List<Group> {
        val groupList = groupRepository.findAllById(ids)
        val idsFound = groupList.map { it.id }

        ids.forEach {
            val wasFound = idsFound.contains(it)

            if (!wasFound) {
                throw NotFoundException()
            }
        }

        return groupList
    }

    fun findAll(): List<Group> {
        return groupRepository.findAll()
    }

    fun create(data: EditGroupDto): Group {
        val newGroup = Group().apply {
            this.groupName = data.groupName
            this.isActive = data.isActive
        }

        val idsPermissions = data.permissionEncryptedIds.map { CryptoProvider.decryptText(it).toLong() }
        newGroup.permissions = permissionService.findMany(idsPermissions).toMutableSet()

        return groupRepository.save(newGroup)
    }

    fun createInBatch(dataList: List<EditGroupDto>): List<Group> {
        val newGroupList = mutableListOf<Group>()

        dataList.forEach { data ->
            val newGroup = Group().apply {
                this.groupName = data.groupName
                this.isActive = data.isActive
            }

            val idsPermissions = data.permissionEncryptedIds.map { CryptoProvider.decryptText(it).toLong() }
            newGroup.permissions = permissionService.findMany(idsPermissions).toMutableSet()

            newGroupList.add(newGroup)
        }

        return groupRepository.saveAll(newGroupList)
    }

    fun update(id: Long, data: EditGroupDto): Group {
        val groupFound = findOne(id)

        groupFound.apply {
            this.groupName = data.groupName
            this.isActive = data.isActive
        }

        val idsPermissions = data.permissionEncryptedIds.map { CryptoProvider.decryptText(it).toLong() }
        groupFound.permissions = permissionService.findMany(idsPermissions).toMutableSet()

        return groupRepository.save(groupFound)
    }

    fun delete(id: Long) {
        val wasFound = groupRepository.existsById(id)

        if (!wasFound) {
            throw NotFoundException()
        }
        groupRepository.deleteById(id)
    }

}