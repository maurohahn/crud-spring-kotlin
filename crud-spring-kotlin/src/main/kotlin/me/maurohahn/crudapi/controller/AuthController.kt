package me.maurohahn.crudapi.controller

import me.maurohahn.crudapi.auth.AppAuthenticationManager
import me.maurohahn.crudapi.auth.TokenProvider
import me.maurohahn.crudapi.dto.CredentialsDto
import me.maurohahn.crudapi.dto.AuthDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authManager: AppAuthenticationManager,
    private val tokenProvider: TokenProvider,
) {

    @Operation(summary = "login")
    @PostMapping("/login")
    @CrossOrigin
    fun login(@RequestBody @Valid credentials: CredentialsDto): ResponseEntity<AuthDto> {

        val userAuth = UsernamePasswordAuthenticationToken(credentials.email, credentials.password)

        val authentication = authManager.authenticate(userAuth)

        val token = tokenProvider.createToken(authentication)

        val response = AuthDto().apply {
            this.token = token
        }

        return ResponseEntity.status(HttpStatus.OK).body(response)

    }

}