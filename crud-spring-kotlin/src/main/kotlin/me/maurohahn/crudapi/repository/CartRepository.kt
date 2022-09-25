package me.maurohahn.crudapi.repository

import me.maurohahn.crudapi.entity.Cart
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository : JpaRepository<Cart, Long> {

}