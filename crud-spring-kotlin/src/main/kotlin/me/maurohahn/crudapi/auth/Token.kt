package me.maurohahn.crudapi.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * @param sub the Subject (username)
 * @param iat the IssuedAt (timestamp)
 * @param exp the Expiration (timestamp)
 * @param role the Role (Two-Factor Authentication (2FA)
 * @param permissions the Permission Authorities
 */
data class Token(

    var sub: String? = null,

    var iat: Long? = null,

    var exp: Long? = null,

    var role: Role? = null,

    var permissions: List<String> = mutableListOf()
) {

    /** Convert roles (need prefix "ROLE_") and authorities to grantedAuthority list. */
    fun toGrantedAuthorities(): List<GrantedAuthority> {

        val grantedAuthorities = mutableListOf<GrantedAuthority>()

        if (role != null) {
            grantedAuthorities.add(role!!.toGrantedAuthority())
        }

        grantedAuthorities.addAll(permissions.map { SimpleGrantedAuthority(it) })

        return grantedAuthorities
    }

}