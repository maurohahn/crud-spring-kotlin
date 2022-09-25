package me.maurohahn.crudapi.repository

import me.maurohahn.crudapi.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

}