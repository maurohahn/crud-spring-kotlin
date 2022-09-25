package me.maurohahn.crudapi.auth

import me.maurohahn.crudapi.exception.UnauthorizedException
import me.maurohahn.crudapi.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class AppUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw UnauthorizedException()

        if (user.isActive != true) {
            throw UnauthorizedException()
        }

        val authoritiesNames = mutableListOf<String>()

        user.groups.forEach { group ->
            if (group.isActive == true) {

                group.permissions.forEach { permission ->
                    if (permission.isActive == true) {
                        authoritiesNames.add(permission.permissionName.toString())
                    }
                }
            }
        }

        val authorities = mutableListOf<GrantedAuthority>()
        if (authoritiesNames.isNotEmpty()) {

            authoritiesNames.distinct().forEach { roleName ->
                authorities.add(SimpleGrantedAuthority(roleName))
            }

        }
        return User(
            user.email,
            user.password,
            authorities
        )
    }

    fun loadUserByClaims(claims: Jws<Claims>): UserDetails {
        val email = claims.body.subject
        val authoritiesFromToken = claims.body["auth"] as List<*>

        val authorities = ArrayList<GrantedAuthority>()

        authoritiesFromToken.forEach { authority ->
            authorities.add(SimpleGrantedAuthority(authority.toString()))
        }

        return User(
            email,
            "",
            authorities
        )
    }
}