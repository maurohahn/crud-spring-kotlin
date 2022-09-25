package me.maurohahn.crudapi.service

import me.maurohahn.crudapi.dto.CredentialsDto
import me.maurohahn.crudapi.dto.EditUserDto
import me.maurohahn.crudapi.entity.User
import me.maurohahn.crudapi.exception.NotFoundException
import me.maurohahn.crudapi.repository.UserRepository
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val groupService: GroupService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    fun findOne(id: Long): User {
        return userRepository.findById(id).orElseThrow { NotFoundException() }
    }

    fun findMany(ids: List<Long>): List<User> {
        val userList = userRepository.findAllById(ids)
        val idsFound = userList.map { it.id }

        ids.forEach {
            val wasFound = idsFound.contains(it)

            if (!wasFound) {
                throw NotFoundException()
            }
        }

        return userList
    }

    fun findByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw NotFoundException()
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun create(data: EditUserDto): User {
        val newUser = User().apply {
            this.name = data.name
            this.password = bCryptPasswordEncoder.encode(data.password)
            this.email = data.email
            this.isActive = data.isActive
        }
        val idsGroups = data.groupEncryptedIds.map { CryptoProvider.decryptGen(it).toLong() }
        newUser.groups = groupService.findMany(idsGroups).toSet()

        return userRepository.save(newUser)
    }

    fun createInBatch(dataList: List<EditUserDto>): List<User> {
        val newUserList = mutableListOf<User>()

        dataList.forEach { data ->
            val newUser = User().apply {
                this.name = data.name
                this.password = data.password
                this.email = data.email
                this.isActive = data.isActive
            }

            val idsGroups = data.groupEncryptedIds.map { CryptoProvider.decryptGen(it).toLong() }
            newUser.groups = groupService.findMany(idsGroups).toSet()

            newUserList.add(newUser)
        }

        return userRepository.saveAll(newUserList)
    }

    fun update(id: Long, data: EditUserDto): User {
        val userFound = findOne(id)

        userFound.apply {
            this.name = data.name
            this.password = data.password
            this.email = data.email
            this.isActive = data.isActive
        }

        val idsGroups = data.groupEncryptedIds.map { CryptoProvider.decryptGen(it).toLong() }
        userFound.groups = groupService.findMany(idsGroups).toSet()

        return userRepository.save(userFound)
    }

    fun resetUserPassword(data: CredentialsDto) {
        val (email, password) = data

        val userFound = findByEmail(email)

        userFound.password = bCryptPasswordEncoder.encode(password)

        userRepository.save(userFound)

    }

    fun delete(id: Long) {
        findOne(id) // validate

        userRepository.deleteById(id)
    }

}
