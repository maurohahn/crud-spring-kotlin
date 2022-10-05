package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
class User : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dp_erp_users_seq")
    @SequenceGenerator(name = "dp_erp_users_seq", sequenceName = "dp_erp_users_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "password")
    var password: String? = null

    @Column(name = "email", unique = true)
    var email: String? = null

    @Column(name = "is_active")
    var isActive: Boolean? = null

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "users_groups",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")]
    )
    var groups: MutableSet<Group> = mutableSetOf()

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_at")
    var lastLoginAt: Date? = null

    @PostLoad
    @PostPersist
    private fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptText(id.toString())

    }

}