package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import javax.persistence.*

@Entity
@Table(name = "customers")
class Customer : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_seq")
    @SequenceGenerator(name = "customers_seq", sequenceName = "customers_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "cpf", unique = true)
    var cpf: String? = null

    @Column(name = "email", unique = true)
    var email: String? = null

    @Column(name = "address")
    var address: String? = null

    @Column(name = "city")
    var city: String? = null

    @Column(name = "cep")
    var cep: String? = null

    @Column(name = "uf")
    var uf: String? = null

    @Column(name = "is_active")
    var isActive: Boolean? = null

    @PostLoad
    @PostPersist
    fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptText(id.toString())
    }

}