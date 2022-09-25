package me.maurohahn.crudapi.controller

import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.dto.CartDto
import me.maurohahn.crudapi.dto.EditCartDto
import me.maurohahn.crudapi.service.CartService
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/cart")
class CartController(private val service: CartService) {

    @Operation(summary = "find a cart by id")
    @GetMapping("/{encryptedId}")
    fun findOne(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<CartDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val cart = service.findOne(id)
        val result = CartDto(cart)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "returns all carts")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<CartDto>> {
        val cartsList = service.findAll()

        val resultList = cartsList.map { CartDto(it) }

        return ResponseEntity(resultList, HttpStatus.OK)
    }

    @Operation(summary = "create a cart")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun create(@RequestBody @Valid data: EditCartDto): ResponseEntity<CartDto> {
        val cartSaved = service.create(data)
        val result = CartDto(cartSaved)

        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @Operation(summary = "create batch carts")
    @PostMapping("/create-in-batch")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun createInBatch(@RequestBody @Valid data: List<EditCartDto>):
            ResponseEntity<List<CartDto>> {
        val cartListSaved = service.createInBatch(data)
        val resultList = cartListSaved.map { CartDto(it) }

        return ResponseEntity(resultList, HttpStatus.CREATED)
    }

    @Operation(summary = "update a cart")
    @PutMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun update(@PathVariable("encryptedId") encryptedId: String, @RequestBody @Valid data: EditCartDto):
            ResponseEntity<CartDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val cartUpdated = service.update(id, data)
        val result = CartDto(cartUpdated)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "delete a cart")
    @DeleteMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun delete(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<Any> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        service.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}