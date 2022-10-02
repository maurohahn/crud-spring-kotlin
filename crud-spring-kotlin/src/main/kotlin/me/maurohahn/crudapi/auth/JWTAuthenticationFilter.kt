package me.maurohahn.crudapi.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.maurohahn.crudapi.dto.CredentialsDto
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
    private val authManager: AuthManager,
    private val authService: AuthService
) : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest, res: HttpServletResponse?
    ): AuthenticationToken {
        try {

            val mapper = jacksonObjectMapper()
            val (email, password) = mapper.readValue<CredentialsDto>(req.inputStream)

            val token = authService.loginWithCredentials(email, password)
            val authToken = AuthenticationToken(token)

            return authManager.authenticate(authToken)
        } catch (e: Exception) {
            throw AuthenticationServiceException(e.message)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain?,
        authentication: Authentication
    ) {

        if (authentication is AuthenticationToken) {
            val token = authentication.getToken()
            res.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }

    }

}