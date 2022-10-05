package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import javax.persistence.*

@Entity
@Table(name = "order_items")
class OrderItem : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    @SequenceGenerator(name = "orders_seq", sequenceName = "orders_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "order_id")
    var orderId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product? = null

    @Column(name = "quantity")
    var quantity: Int? = null

    @PostLoad
    @PostPersist
    private fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptText(id.toString())
    }

}
