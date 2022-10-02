package me.maurohahn.crudapi.service

import me.maurohahn.crudapi.dto.EditCartDto
import me.maurohahn.crudapi.entity.Cart
import me.maurohahn.crudapi.exception.NotFoundException
import me.maurohahn.crudapi.repository.CartRepository
import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val customerService: CustomerService,
    private val productService: ProductService,
) {

    fun findOne(id: Long): Cart {
        return cartRepository.findById(id).orElseThrow { NotFoundException() }
    }

    fun findMany(ids: List<Long>): List<Cart> {
        val cartList = cartRepository.findAllById(ids)
        val idsFound = cartList.map { it.id }

        ids.forEach {
            val wasFound = idsFound.contains(it)

            if (!wasFound) {
                throw NotFoundException()
            }
        }

        return cartList
    }

    fun findAll(): List<Cart> {
        return cartRepository.findAll()
    }

    fun create(data: EditCartDto): Cart {
        val newCart = Cart()

        val customerId = CryptoProvider.decryptText(data.customerEncryptedId).toLong()
        customerService.verifyOne(customerId)


//        val customer = customerService.findOne(customerId)
        newCart.customerId = customerId

        val idsProducts = data.productEncryptedIds.map { CryptoProvider.decryptText(it).toLong() }
        newCart.products = productService.findMany(idsProducts).toMutableSet()


        return cartRepository.save(newCart)
    }

    fun createInBatch(dataList: List<EditCartDto>): List<Cart> {
        val newCartList = mutableListOf<Cart>()

        dataList.forEach { data ->
            val newCart = Cart()

            val customerId = CryptoProvider.decryptText(data.customerEncryptedId).toLong()
            val customer = customerService.findOne(customerId)
            newCart.customerId = customer.id

            val idsProducts = data.productEncryptedIds.map { CryptoProvider.decryptText(it).toLong() }
            newCart.products = productService.findMany(idsProducts).toMutableSet()

            newCartList.add(newCart)
        }

        return cartRepository.saveAll(newCartList)
    }

    fun update(id: Long, data: EditCartDto): Cart {
        val cartFound = findOne(id)

        val customerId = CryptoProvider.decryptText(data.customerEncryptedId).toLong()
        val customer = customerService.findOne(customerId)
        cartFound.customerId = customer.id

        val idsProducts = data.productEncryptedIds.map { CryptoProvider.decryptText(it).toLong() }
        cartFound.products = productService.findMany(idsProducts).toMutableSet()

        return cartRepository.save(cartFound)
    }

    fun delete(id: Long) {
        findOne(id) // validate

        cartRepository.deleteById(id)
    }
}