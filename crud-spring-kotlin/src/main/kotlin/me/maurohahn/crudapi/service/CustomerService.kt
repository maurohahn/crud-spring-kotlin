package me.maurohahn.crudapi.service

import me.maurohahn.crudapi.dto.EditCustomerDto
import me.maurohahn.crudapi.entity.Customer
import me.maurohahn.crudapi.exception.NotFoundException
import me.maurohahn.crudapi.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) {

    fun findOne(id: Long): Customer {

        return customerRepository.findById(id).orElseThrow { NotFoundException() }
    }

    fun findMany(ids: List<Long>): List<Customer> {
        val customerList = customerRepository.findAllById(ids)
        val idsFound = customerList.map { it.id }

        ids.forEach {
            val wasFound = idsFound.contains(it)

            if (!wasFound) {
                throw NotFoundException()
            }
        }

        return customerList
    }

    fun findAll(): List<Customer> {
        return customerRepository.findAll()
    }

    fun create(data: EditCustomerDto): Customer {
        val newCustomer = Customer().apply {
            this.name = data.name
            this.cpf = data.cpf
            this.email = data.email
            this.address = data.address
            this.city = data.city
            this.cep = data.cep
            this.uf = data.uf
            this.isActive = data.isActive
        }

        return customerRepository.save(newCustomer)
    }

    fun createInBatch(dataList: List<EditCustomerDto>): List<Customer> {
        val newCustomerList = mutableListOf<Customer>()

        dataList.forEach { data ->
            val newCustomer = Customer().apply {
                this.name = data.name
                this.cpf = data.cpf
                this.email = data.email
                this.address = data.address
                this.city = data.city
                this.cep = data.cep
                this.uf = data.uf
                this.isActive = data.isActive
            }

            newCustomerList.add(newCustomer)
        }

        return customerRepository.saveAll(newCustomerList)
    }

    fun update(id: Long, data: EditCustomerDto): Customer {
        val customerFound = findOne(id)

        customerFound.apply {
            this.name = data.name
            this.cpf = data.cpf
            this.email = data.email
            this.address = data.address
            this.city = data.city
            this.cep = data.cep
            this.uf = data.uf
            this.isActive = data.isActive
        }

        return customerRepository.save(customerFound)
    }

    fun delete(id: Long) {
        findOne(id) // validate

        customerRepository.deleteById(id)
    }

}