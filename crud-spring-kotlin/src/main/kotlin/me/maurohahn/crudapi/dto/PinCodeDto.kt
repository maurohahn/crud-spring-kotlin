package me.maurohahn.crudapi.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class PinCodeDto(
    @Email
    @NotNull(message = "email cannot be null")
    var email: String,

    @NotNull(message = "code cannot be null")
    var code: Int

)