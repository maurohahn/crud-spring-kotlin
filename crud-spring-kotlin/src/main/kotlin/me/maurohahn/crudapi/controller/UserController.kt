package me.maurohahn.crudapi.controller

import me.maurohahn.crudapi.dto.CredentialsDto
import me.maurohahn.crudapi.dto.EditUserDto
import me.maurohahn.crudapi.service.UserService
import me.maurohahn.crudapi.dto.UserDto
import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(private val service: UserService) {

    @Operation(summary = "find a user by id")
    @GetMapping("/{encryptedId}")
    fun findOne(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<UserDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val user = service.findOne(id)
        val result = UserDto(user)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "returns all users")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<UserDto>> {
        val userList = service.findAll()

        val resultList = userList.map { UserDto(it) }

        return ResponseEntity(resultList, HttpStatus.OK)
    }

    @Operation(summary = "create a user")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun create(@RequestBody @Valid data: EditUserDto): ResponseEntity<UserDto> {
        val userSaved = service.create(data)
        val result = UserDto(userSaved)

        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @Operation(summary = "create batch users")
    @PostMapping("/create-in-batch")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun createInBatch(@RequestBody @Valid data: List<EditUserDto>):
            ResponseEntity<List<UserDto>> {
        val userListSaved = service.createInBatch(data)
        val resultList = userListSaved.map { UserDto(it) }

        return ResponseEntity(resultList, HttpStatus.CREATED)
    }

    @Operation(summary = "update a user")
    @PutMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun update(
        @PathVariable("encryptedId") encryptedId: String,
        @RequestBody @Valid data: EditUserDto
    ): ResponseEntity<UserDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val groupUpdated = service.update(id, data)
        val result = UserDto(groupUpdated)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "reset password a user")
    @PatchMapping("/reset-password")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun resetUserPassword(
        @RequestBody @Valid data: CredentialsDto
    ): ResponseEntity<UserDto> {
        service.resetUserPassword(data)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Operation(summary = "delete a user")
    @DeleteMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun delete(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<Any> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        service.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}