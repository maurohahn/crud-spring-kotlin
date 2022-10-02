package me.maurohahn.crudapi.controller

import me.maurohahn.crudapi.dto.EditGroupDto
import me.maurohahn.crudapi.service.GroupService
import me.maurohahn.crudapi.dto.GroupDto
import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/group")
class GroupController(private val service: GroupService) {

    @PreAuthorize("hasAnyAuthority('ADMIN','GROUP_READ')")
    @Operation(summary = "find a group by id")
    @GetMapping("/{encryptedId}")
    fun findOne(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<GroupDto> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        val group = service.findOne(id)
        val result = GroupDto(group)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GROUP_READ')")
    @Operation(summary = "returns all groups")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<GroupDto>> {
        val groupList = service.findAll()

        val resultList = groupList.map { GroupDto(it) }

        return ResponseEntity(resultList, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GROUP_WRITE')")
    @Operation(summary = "create a group")
    @PostMapping("/")
    fun create(@RequestBody @Valid data: EditGroupDto): ResponseEntity<GroupDto> {
        val groupSaved = service.create(data)
        val result = GroupDto(groupSaved)

        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GROUP_WRITE')")
    @Operation(summary = "create batch groups")
    @PostMapping("/create-in-batch")
    fun createInBatch(@RequestBody @Valid data: List<EditGroupDto>):
            ResponseEntity<List<GroupDto>> {
        val groupListSaved = service.createInBatch(data)
        val resultList = groupListSaved.map { GroupDto(it) }

        return ResponseEntity(resultList, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GROUP_WRITE')")
    @Operation(summary = "update a group")
    @PutMapping("/{encryptedId}")
    fun update(@PathVariable("encryptedId") encryptedId: String, @RequestBody @Valid data: EditGroupDto):
            ResponseEntity<GroupDto> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        val groupUpdated = service.update(id, data)
        val result = GroupDto(groupUpdated)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GROUP_DELETE')")
    @Operation(summary = "delete a group")
    @DeleteMapping("/{encryptedId}")
    fun delete(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<Any> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        service.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}