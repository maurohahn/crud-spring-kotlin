package me.maurohahn.crudapi.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class TokenProvider(
    private val userDetailsService: AppUserDetailsService,
    @Value("\${api.jwt.secret}")
    private val secret: String,
    @Value("\${api.jwt.expiration}")
    private val expiration: Long,
) {

    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun createToken(authentication: Authentication): String {
        val iat = System.currentTimeMillis()
        val exp = iat + TimeUnit.HOURS.toMillis(expiration)

        val authClaims: MutableList<String> = mutableListOf()

        authentication.authorities?.let { authorities ->
            authorities.forEach { claim -> authClaims.add(claim.toString()) }
        }


        return Jwts.builder()
            .setSubject(authentication.name)
            .claim("auth", authClaims)
            .setIssuedAt(Date())
            .setExpiration(Date(exp))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun refreshToken(token: String): String {
        val iat = System.currentTimeMillis()
        val exp = iat + TimeUnit.HOURS.toMillis(expiration)

        val claims = getClaimsToken(token)

        if (claims != null) {
            claims.issuedAt = Date(iat)
            claims.expiration = Date(exp)
        }

        return Jwts.builder().setClaims(claims).signWith(key, SignatureAlgorithm.HS512).compact()
    }

    fun isTokenValid(token: String): Boolean {
        val claims = getClaimsToken(token)
        if (claims != null) {
            val username = claims.subject
            val expirationDate = claims.expiration
            val now = Date(System.currentTimeMillis())
            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true
            }
        }
        return false
    }

    private fun getClaimsToken(token: String): Claims? {

        return try {

            val tokenValue = token.replace("Bearer ", "")
            Jwts.parserBuilder().setSigningKey(secret.toByteArray()).build().parseClaimsJws(tokenValue).body
        } catch (e: Exception) {
            null
        }
    }

    fun getAuthentication(token: String): Authentication? {
        return try {
            val tokenValue = token.replace("Bearer ", "")

            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(tokenValue)

            val principal = userDetailsService.loadUserByClaims(claims)

            UsernamePasswordAuthenticationToken(principal, tokenValue, principal.authorities)

        } catch (e: Exception) {
            null
        }
    }

}