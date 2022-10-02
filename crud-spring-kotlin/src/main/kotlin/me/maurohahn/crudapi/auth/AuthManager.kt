package me.maurohahn.crudapi.auth

import me.maurohahn.crudapi.exception.InvalidUsernameOrPassword
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class AuthManager(private val authService: AuthService) : AuthenticationManager {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): AuthenticationToken {

        if (authentication is AuthenticationToken) {

            val email = authentication.name
            val password = authentication.credentials.toString()
            val token = authService.loginWithCredentials(email, password)

            return AuthenticationToken(token)
        } else {
            throw InvalidUsernameOrPassword()
        }

    }

}
