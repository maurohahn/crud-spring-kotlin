package me.maurohahn.crudapi.repository

import me.maurohahn.crudapi.entity.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<Group, Long> {

}