package me.maurohahn.crudapi.controller

import io.swagger.v3.oas.annotations.Operation
import me.maurohahn.crudapi.auth.AuthService
import me.maurohahn.crudapi.auth.TokenProvider
import me.maurohahn.crudapi.dto.AuthDto
import me.maurohahn.crudapi.dto.CredentialsDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val tokenProvider: TokenProvider,
) {

    @Operation(summary = "login")
    @PostMapping("/login")
    @CrossOrigin
    fun login(@RequestBody @Valid credentials: CredentialsDto): ResponseEntity<AuthDto> {

        val token = authService.loginWithCredentials(credentials.email, credentials.password)

        val response = AuthDto().apply {
            this.token = tokenProvider.encode(token)
        }

        return ResponseEntity.status(HttpStatus.OK).body(response)

    }

//    @PreAuthorize("hasAnyRole('PRE_AUTH_USER')")
//    @Operation(summary = "login-2fa")
//    @PostMapping("/login/2fa")
//    fun login2fa(@RequestBody @Valid pinCode: PinCodeDto): ResponseEntity<AuthDto> {
//
//        val token = authService.loginWithCode(pinCode.email, pinCode.code)
//
//        val response = AuthDto().apply {
//            this.token = tokenProvider.encode(token)
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(response)
//
//    }

//    @Operation(summary = "logout")
//    @PostMapping("/logout")
//    fun logout(
//        authentication: AuthenticationToken,
//        req: HttpServletRequest
//    ): ResponseEntity<Unit> {
//
//        // authService.logout(req)
//
//        return ResponseEntity.status(HttpStatus.OK).build()
//
//    }

}