package me.maurohahn.crudapi.dto

import me.maurohahn.crudapi.entity.Customer

data class CustomerDto(

    var encryptedId: String? = null,

    var name: String? = null,

    var cpf: String? = null,

    var email: String? = null,

    var address: String? = null,

    var city: String? = null,

    var cep: String? = null,

    var uf: String? = null,

    var isActive: Boolean? = null

) {

    constructor(data: Customer) : this() {
        encryptedId = data.encryptedId
        name = data.name
        cpf = data.cpf
        email = data.email
        address = data.address
        city = data.city
        cep = data.cep
        uf = data.uf
        isActive = data.isActive
    }

}