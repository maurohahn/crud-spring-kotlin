package me.maurohahn.crudapi.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CredentialsDto(
    @Email
    @NotNull(message = "email cannot be null")
    var email: String,
    @Size(min = 8, message = "password cannot be less than 8")
    @NotNull(message = "password cannot be null")
    var password: String

)