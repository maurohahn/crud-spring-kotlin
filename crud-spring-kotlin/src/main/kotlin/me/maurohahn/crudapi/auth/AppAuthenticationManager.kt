package me.maurohahn.crudapi.auth

import me.maurohahn.crudapi.exception.UnauthorizedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class AppAuthenticationManager(
    private val appUserService: AppUserDetailsService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
) : AuthenticationManager {
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val password = authentication.credentials.toString()
        val user = appUserService.loadUserByUsername(authentication.name)

        if (!bCryptPasswordEncoder.matches(password, user.password)) {
            throw UnauthorizedException()
        }
        return UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
    }
}
