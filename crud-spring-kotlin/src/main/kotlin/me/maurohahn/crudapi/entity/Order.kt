package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*

@Entity
@Table(name = "orders")
class Order : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    @SequenceGenerator(name = "orders_seq", sequenceName = "orders_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "customer_id", insertable = false, updatable = false)
    var customerId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null

//    @Fetch(FetchMode.SUBSELECT)
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
//    @JoinColumn(name = "order_id")
//    var items: MutableList<OrderItem> = mutableListOf()

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @OrderBy("updated_at ASC")
    var items: MutableList<OrderItem> = mutableListOf()

    @Transient
    var totalPrice: Float? = null

    @PostLoad
    @PostPersist
    private fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptText(id.toString())
    }

}
