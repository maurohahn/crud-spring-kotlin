package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import javax.persistence.*

@Entity
@Table(name = "carts")
class Cart : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carts_seq")
    @SequenceGenerator(name = "carts_seq", sequenceName = "carts_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "customer_id")
    var customerId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    var customer: Customer? = null

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE]
    )
    @JoinTable(
        name = "products_carts",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "cart_id")]
    )
    var products = setOf<Product>()

    @PostLoad
    @PostPersist
    fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptGen(id.toString())
    }

}
