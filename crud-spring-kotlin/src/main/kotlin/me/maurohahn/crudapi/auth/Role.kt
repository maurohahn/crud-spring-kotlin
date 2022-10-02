package me.maurohahn.crudapi.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Enum for Two-Factor Authentication (2FA).
 *
 */
enum class Role {
    /** First authentication, need 2fa (Pin Code). */
    PRE_AUTH_USER,

    /** Last authentication, done. */
    AUTH_USER;

    fun toGrantedAuthority(): GrantedAuthority {
        return SimpleGrantedAuthority("ROLE_$name")
    }
}