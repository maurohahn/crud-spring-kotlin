package me.maurohahn.crudapi.controller

import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.dto.CustomerDto
import me.maurohahn.crudapi.dto.EditCustomerDto
import me.maurohahn.crudapi.service.CustomerService
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/customer")
class CustomerController(private val service: CustomerService) {

    @Operation(summary = "find a customer by id")
    @GetMapping("/{encryptedId}")
    fun findOne(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<CustomerDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val customer = service.findOne(id)
        val result = CustomerDto(customer)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "returns all customers")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<CustomerDto>> {
        val customerList = service.findAll()

        val resultList = customerList.map { CustomerDto(it) }

        return ResponseEntity(resultList, HttpStatus.OK)
    }

    @Operation(summary = "create a customer")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun create(@RequestBody @Valid data: EditCustomerDto): ResponseEntity<CustomerDto> {
        val customerSaved = service.create(data)
        val result = CustomerDto(customerSaved)

        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @Operation(summary = "create batch customers")
    @PostMapping("/create-in-batch")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun createInBatch(@RequestBody @Valid data: List<EditCustomerDto>):
            ResponseEntity<List<CustomerDto>> {
        val customerListSaved = service.createInBatch(data)
        val resultList = customerListSaved.map { CustomerDto(it) }

        return ResponseEntity(resultList, HttpStatus.CREATED)
    }

    @Operation(summary = "update a customer")
    @PutMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun update(@PathVariable("encryptedId") encryptedId: String, @RequestBody @Valid data: EditCustomerDto):
            ResponseEntity<CustomerDto> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        val customerUpdated = service.update(id, data)
        val result = CustomerDto(customerUpdated)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @Operation(summary = "delete a customer")
    @DeleteMapping("/{encryptedId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun delete(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<Any> {
        val id = CryptoProvider.decryptGen(encryptedId).toLong()
        service.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}