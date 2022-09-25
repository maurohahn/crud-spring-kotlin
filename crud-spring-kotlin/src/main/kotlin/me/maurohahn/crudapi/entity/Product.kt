package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import javax.persistence.*

@Entity
@Table(name = "products")
class Product : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
    @SequenceGenerator(name = "products_seq", sequenceName = "products_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "description", unique = true)
    var description: String? = null

    @Column(name = "price")
    var price: Float? = null

    @Column(name = "brand")
    var brand: String? = null

    @Column(name = "quantity")
    var quantity: Int? = null

    @Column(name = "category")
    var category: String? = null

    @Column(name = "is_active")
    var isActive: Boolean? = null

//    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH], mappedBy = "products")
//    var carts = setOf<Cart>()

    @PostLoad
    @PostPersist
    fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptGen(id.toString())
    }

}