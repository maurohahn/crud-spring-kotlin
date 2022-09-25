package me.maurohahn.crudapi.dto

import javax.validation.constraints.Email

data class EditCustomerDto(

    var name: String? = null,

    var cpf: String? = null,

    @Email
    var email: String? = null,

    var address: String? = null,

    var city: String? = null,

    var cep: String? = null,

    var uf: String? = null,

    var isActive: Boolean? = null,

)