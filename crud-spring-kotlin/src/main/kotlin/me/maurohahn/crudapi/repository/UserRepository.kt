package me.maurohahn.crudapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import me.maurohahn.crudapi.entity.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?
}