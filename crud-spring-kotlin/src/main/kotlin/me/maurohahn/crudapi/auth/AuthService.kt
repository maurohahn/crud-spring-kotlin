package me.maurohahn.crudapi.auth

import me.maurohahn.crudapi.exception.InvalidUsernameOrPassword
import me.maurohahn.crudapi.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    @Throws(UsernameNotFoundException::class)
    fun loginWithCredentials(email: String, password: String): Token {
        val user = userRepository.findByEmail(email) ?: throw InvalidUsernameOrPassword()

        if (user.isActive != true) {
            throw InvalidUsernameOrPassword()
        }

        if (!bCryptPasswordEncoder.matches(password, user.password)) {
            throw InvalidUsernameOrPassword()
        }

        val permissionsNames = mutableListOf<String>()

        user.groups.forEach { group ->
            if (group.isActive == true) {

                group.permissions.forEach { permission ->
                    if (permission.isActive == true && permission.permissionName != null) {
                        permissionsNames.add(permission.permissionName!!)
                    }
                }
            }
        }

        val token = Token().apply {
            this.sub = user.email
            this.permissions = permissionsNames.distinct()
            this.role = Role.AUTH_USER
        }

        return token
    }

    fun loginWithCode(email: String, code: Int): Token {
        // TODO - logic to do the 2fa (eg: pin code - otp)
        return Token()
    }

    fun logout(req: HttpServletRequest) {
        // TODO - logic to remove token (need to persist the token eg: redis)
    }

}