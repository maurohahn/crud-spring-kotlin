package me.maurohahn.crudapi.controller

import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.dto.EditProductDto
import me.maurohahn.crudapi.dto.ProductDto
import me.maurohahn.crudapi.service.ProductService
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/product")
class ProductController(private val service: ProductService) {

    @Operation(summary = "find a product by id")
    @GetMapping("/{encryptedId}")
    fun findOne(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<ProductDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val product = service.findOne(id)
        val result = ProductDto(product)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "returns all products")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<ProductDto>> {
        val productList = service.findAll()

        val resultList = productList.map { ProductDto(it) }

        return ResponseEntity(resultList, HttpStatus.OK)
    }

    @Operation(summary = "create a product")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun create(@RequestBody @Valid data: EditProductDto): ResponseEntity<ProductDto> {
        val productSaved = service.create(data)
        val result = ProductDto(productSaved)

        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @Operation(summary = "create batch products")
    @PostMapping("/create-in-batch")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun createInBatch(@RequestBody @Valid data: List<EditProductDto>):
            ResponseEntity<List<ProductDto>> {
        val productListSaved = service.createInBatch(data)
        val resultList = productListSaved.map { ProductDto(it) }

        return ResponseEntity(resultList, HttpStatus.CREATED)
    }

    @Operation(summary = "update a product")
    @PutMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun update(@PathVariable("encryptedId") encryptedId: String, @RequestBody @Valid data: EditProductDto):
            ResponseEntity<ProductDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val productUpdated = service.update(id, data)
        val result = ProductDto(productUpdated)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "delete a product")
    @DeleteMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun delete(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<Any> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        service.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}