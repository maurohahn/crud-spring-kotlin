package me.maurohahn.crudapi.controller

import me.maurohahn.crudapi.dto.EditPermissionDto
import me.maurohahn.crudapi.service.PermissionService
import me.maurohahn.crudapi.dto.PermissionDto
import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/permission")
class PermissionController(private val service: PermissionService) {

    @PreAuthorize("hasAnyAuthority('ADMIN','PERMISSION_READ')")
    @Operation(summary = "find a permission by id")
    @GetMapping("/{encryptedId}")
    fun findOne(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<PermissionDto> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        val permission = service.findOne(id)
        val result = PermissionDto(permission)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PERMISSION_READ')")
    @Operation(summary = "returns all permissions")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<PermissionDto>> {
        val permissionList = service.findAll()

        val resultList = permissionList.map { PermissionDto(it) }

        return ResponseEntity(resultList, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PERMISSION_WRITE')")
    @Operation(summary = "create a permission")
    @PostMapping("/")
    fun create(@RequestBody @Valid data: EditPermissionDto): ResponseEntity<PermissionDto> {
        val permissionSaved = service.create(data)
        val result = PermissionDto(permissionSaved)

        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PERMISSION_WRITE')")
    @Operation(summary = "create batch permissions")
    @PostMapping("/create-in-batch")
    fun createInBatch(@RequestBody @Valid data: List<EditPermissionDto>):
            ResponseEntity<List<PermissionDto>> {
        val permissionListSaved = service.createInBatch(data)
        val resultList = permissionListSaved.map { PermissionDto(it) }

        return ResponseEntity(resultList, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PERMISSION_WRITE')")
    @Operation(summary = "update a permission")
    @PutMapping("/{encryptedId}")
    fun update(@PathVariable("encryptedId") encryptedId: String, @RequestBody @Valid data: EditPermissionDto):
            ResponseEntity<PermissionDto> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        val permissionUpdated = service.update(id, data)
        val result = PermissionDto(permissionUpdated)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PERMISSION_DELETE')")
    @Operation(summary = "delete a permission")
    @DeleteMapping("/{encryptedId}")
    fun delete(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<Unit> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        service.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}