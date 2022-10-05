package me.maurohahn.crudapi.repository

import me.maurohahn.crudapi.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long> {

    fun existsByIdAndIsActiveTrue(id: Long): Boolean

}