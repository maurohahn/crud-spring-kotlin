package me.maurohahn.crudapi.service

import me.maurohahn.crudapi.dto.EditProductDto
import me.maurohahn.crudapi.entity.Product
import me.maurohahn.crudapi.exception.NotFoundException
import me.maurohahn.crudapi.repository.ProductRepository
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    fun findOne(id: Long): Product {
        return productRepository.findById(id).orElseThrow { NotFoundException() }
    }

    fun findMany(ids: List<Long>): List<Product> {
        val productList = productRepository.findAllById(ids)
        val idsFound = productList.map { it.id }

        ids.forEach {
            val wasFound = idsFound.contains(it)

            if (!wasFound) {
                throw NotFoundException()
            }
        }

        return productList
    }

    fun findAll(): List<Product> {
        return productRepository.findAll()
    }

    fun create(data: EditProductDto): Product {
        val newProduct = Product().apply {
            this.description = data.description
            this.price = data.price
            this.brand = data.brand
            this.quantity = data.quantity
            this.category = data.category
            this.isActive = data.isActive
        }

        return productRepository.save(newProduct)
    }

    fun createInBatch(dataList: List<EditProductDto>): List<Product> {
        val newProductList = mutableListOf<Product>()

        dataList.forEach { data ->
            val newProduct = Product().apply {
                this.description = data.description
                this.price = data.price
                this.brand = data.brand
                this.quantity = data.quantity
                this.category = data.category
                this.isActive = data.isActive
            }

            newProductList.add(newProduct)
        }

        return productRepository.saveAll(newProductList)
    }

    fun update(id: Long, data: EditProductDto): Product {
        val productFound = findOne(id)

        productFound.apply {
            this.description = data.description
            this.price = data.price
            this.brand = data.brand
            this.quantity = data.quantity
            this.category = data.category
            this.isActive = data.isActive
        }

        return productRepository.save(productFound)
    }

    fun delete(id: Long) {
        val wasFound = productRepository.existsById(id)

        if (!wasFound) {
            throw NotFoundException()
        }
        productRepository.deleteById(id)
    }

    @PostConstruct
    fun fix(){

        val productList = productRepository.findAll()

        productList.forEach {
            val percent = it.price!! * 0.02.toFloat()
            it.price = it.price!! + percent
        }

        productRepository.saveAll(productList)

        println("finish")
    }

}