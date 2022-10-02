package me.maurohahn.crudapi.auth

import org.springframework.security.authentication.AbstractAuthenticationToken

class AuthenticationToken(private val principal: Token) : AbstractAuthenticationToken(principal.toGrantedAuthorities()) {

    init {
        this.isAuthenticated = true
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return principal
    }

    fun getToken(): Token {
        return principal
    }

}