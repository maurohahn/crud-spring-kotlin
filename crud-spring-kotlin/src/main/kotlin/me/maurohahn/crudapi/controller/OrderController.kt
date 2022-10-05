package me.maurohahn.crudapi.controller

import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.dto.OrderDto
import me.maurohahn.crudapi.dto.EditOrderDto
import me.maurohahn.crudapi.service.OrderService
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/order")
class OrderController(private val service: OrderService) {

    @PreAuthorize("hasAnyAuthority('ADMIN','ORDER_READ')")
    @Operation(summary = "find a order by id")
    @GetMapping("/{encryptedId}")
    fun findOne(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<OrderDto> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        val order = service.findOne(id)
        val result = OrderDto(order)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ORDER_READ')")
    @Operation(summary = "returns all orders")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<OrderDto>> {
        val orderList = service.findAll()

        val resultList = orderList.map { OrderDto(it) }

        return ResponseEntity(resultList, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ORDER_WRITE')")
    @Operation(summary = "create a order")
    @PostMapping("/")
    fun create(@RequestBody @Valid data: EditOrderDto): ResponseEntity<OrderDto> {
        val orderSaved = service.create(data)
        val result = OrderDto(orderSaved)

        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ORDER_WRITE')")
    @Operation(summary = "create batch orders")
    @PostMapping("/create-in-batch")
    fun createInBatch(@RequestBody @Valid data: List<EditOrderDto>):
            ResponseEntity<List<OrderDto>> {
        val orderListSaved = service.createInBatch(data)
        val resultList = orderListSaved.map { OrderDto(it) }

        return ResponseEntity(resultList, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ORDER_WRITE')")
    @Operation(summary = "update a order")
    @PutMapping("/{encryptedId}")
    fun update(@PathVariable("encryptedId") encryptedId: String, @RequestBody @Valid data: EditOrderDto):
            ResponseEntity<OrderDto> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        val orderUpdated = service.update(id, data)
        val result = OrderDto(orderUpdated)

        return ResponseEntity(result, HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ORDER_DELETE')")
    @Operation(summary = "delete a order")
    @DeleteMapping("/{encryptedId}")
    fun delete(@PathVariable("encryptedId") encryptedId: String): ResponseEntity<Unit> {
        val id = CryptoProvider.decryptText(encryptedId).toLong()
        service.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}