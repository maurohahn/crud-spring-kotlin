package me.maurohahn.crudapi.service

import me.maurohahn.crudapi.dto.EditOrderDto
import me.maurohahn.crudapi.entity.Order
import me.maurohahn.crudapi.entity.OrderItem
import me.maurohahn.crudapi.exception.NotFoundException
import me.maurohahn.crudapi.repository.OrderRepository
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val customerService: CustomerService,
    private val productService: ProductService,
) {

    fun findOne(id: Long): Order {
        val order = orderRepository.findById(id).orElseThrow { NotFoundException() }
        prepareTotalPrice(order)
        return order
    }

    fun findMany(ids: List<Long>): List<Order> {
        val orderList = orderRepository.findAllById(ids)
        val idsFound = orderList.map { it.id }

        ids.forEach {
            val wasFound = idsFound.contains(it)

            if (!wasFound) {
                throw NotFoundException()
            }
        }

        return orderList
    }

    fun findAll(): List<Order> {
        val orderList = orderRepository.findAll()

        orderList.forEach { order ->
            prepareTotalPrice(order)
        }

        return orderList
    }

    fun create(data: EditOrderDto): Order {
        val newOrder = Order()
        val newOrderItemList = mutableListOf<OrderItem>()

        val customerId = CryptoProvider.decryptText(data.customerEncryptedId).toLong()
        val customer = customerService.findOne(customerId)

        val idsProducts = data.items.map { CryptoProvider.decryptText(it.productEncryptedId).toLong() }
        val productList = productService.findMany(idsProducts)

        data.items.map {

            val newOrderItem = OrderItem().apply {
                this.product = productList.find { product -> product.encryptedId == it.productEncryptedId }
                this.quantity = it.quantity
            }

            newOrderItemList.add(newOrderItem)
        }

        newOrder.apply {
            this.customer = customer
            this.items.clear()
            this.items.addAll(newOrderItemList)
        }

        prepareTotalPrice(newOrder)

        return orderRepository.save(newOrder)
    }

    fun createInBatch(dataList: List<EditOrderDto>): List<Order> {
        val newOrderList = mutableListOf<Order>()

        dataList.forEach { data ->
            val newOrder = Order()
            val newOrderItemList = mutableListOf<OrderItem>()

            val customerId = CryptoProvider.decryptText(data.customerEncryptedId).toLong()
            val customer = customerService.findOne(customerId)

            val idsProducts = data.items.map { CryptoProvider.decryptText(it.productEncryptedId).toLong() }
            val productList = productService.findMany(idsProducts)

            data.items.map {

                val newOrderItem = OrderItem().apply {
                    this.product = productList.find { product -> product.encryptedId == it.productEncryptedId }
                    this.quantity = it.quantity
                }

                newOrderItemList.add(newOrderItem)
            }

            newOrder.apply {
                this.customer = customer
                this.items.clear()
                this.items.addAll(newOrderItemList)
            }

            prepareTotalPrice(newOrder)

            newOrderList.add(newOrder)
        }

        return orderRepository.saveAll(newOrderList)
    }

    fun update(id: Long, data: EditOrderDto): Order {
        val orderFound = findOne(id)
        val newOrderItemList = mutableListOf<OrderItem>()

        val customerId = CryptoProvider.decryptText(data.customerEncryptedId).toLong()
        val customer = customerService.findOne(customerId)

        val idsProducts = data.items.map { CryptoProvider.decryptText(it.productEncryptedId).toLong() }
        val productList = productService.findMany(idsProducts)

        data.items.map {

            val newOrderItem = OrderItem().apply {
                this.product = productList.find { product -> product.encryptedId == it.productEncryptedId }
                this.quantity = it.quantity
            }

            newOrderItemList.add(newOrderItem)
        }

        orderFound.apply {
            this.customer = customer
            this.items.clear()
            this.items.addAll(newOrderItemList)
        }

        prepareTotalPrice(orderFound)

        return orderRepository.save(orderFound)
    }

    fun delete(id: Long) {
        val wasFound = orderRepository.existsById(id)

        if (!wasFound) {
            throw NotFoundException()
        }
        orderRepository.deleteById(id)
    }

    private fun prepareTotalPrice(data: Order) {
        val calcTotalPrice = data.items.map {
            val price = (it.product?.price ?: 0.0).toFloat()
            val quantity = it.quantity ?: 0
            price * quantity
        }.sum()

        data.totalPrice = calcTotalPrice

    }
}