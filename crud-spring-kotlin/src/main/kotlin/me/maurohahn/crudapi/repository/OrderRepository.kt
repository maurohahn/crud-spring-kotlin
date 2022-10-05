package me.maurohahn.crudapi.repository

import me.maurohahn.crudapi.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {

}