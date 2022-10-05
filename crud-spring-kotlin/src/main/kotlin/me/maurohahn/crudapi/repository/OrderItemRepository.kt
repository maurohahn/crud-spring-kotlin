package me.maurohahn.crudapi.repository

import me.maurohahn.crudapi.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long> {

}