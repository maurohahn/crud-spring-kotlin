package me.maurohahn.crudapi.auth

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Component
class TokenProvider(
    @Value("\${api.jwt.secret}")
    private val secret: String,
    @Value("\${api.jwt.expiration}")
    private val expiration: Long,
) {

    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    private val mapper = jacksonObjectMapper()

    @Throws(JsonProcessingException::class)
    fun encode(jwtObj: Token): String {
        val iat = System.currentTimeMillis()
        val exp = iat + TimeUnit.HOURS.toMillis(expiration)

        jwtObj.also {
            it.iat = iat
            it.exp = exp
        }

        val payload = mapper.writeValueAsString(jwtObj)

        return Jwts.builder()
            .setPayload(payload)
//            .claim("auth", authClaims)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    @Throws(IOException::class)
    fun decode(jwtString: String): Token {

        val payload = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parse(jwtString)
            .body

        val json = mapper.writeValueAsString(payload)

        return mapper.readValue(json, Token::class.java)
    }

    fun refreshToken(headerAuth: String): String {
        val authentication = getAuthentication(headerAuth)

        val iat = System.currentTimeMillis()
        val exp = iat + TimeUnit.HOURS.toMillis(expiration)

        val newToken = authentication?.getToken()!!

        newToken.apply {
            this.iat = iat
            this.exp = exp
        }

        return encode(newToken)
    }

    fun isTokenValid(headerAuth: String): Boolean {
        val authentication = getAuthentication(headerAuth)

        val token = authentication?.getToken()

        if (token != null) {
            val username = token.sub

            val expirationDate = token.exp ?: 0
            val now = System.currentTimeMillis()

            if (username != null && expirationDate >= now) {
                return true
            }
        }
        return false
    }

    fun getAuthentication(headerAuth: String): AuthenticationToken? {
        return try {
            val token = headerAuth.replace("Bearer ", "")

            val principal = decode(token)
            AuthenticationToken(principal)

        } catch (e: Exception) {
            null
        }
    }

}