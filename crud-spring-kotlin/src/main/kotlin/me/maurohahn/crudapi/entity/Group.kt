package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import javax.persistence.*

@Entity
@Table(name = "groups")
class Group : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dp_erp_groups_seq")
    @SequenceGenerator(name = "dp_erp_groups_seq", sequenceName = "dp_erp_groups_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "group_name", unique = true)
    var groupName: String? = null

    @Column(name = "is_active")
    var isActive: Boolean? = null

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE]
    )
    @JoinTable(
        name = "permissions_groups",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: MutableSet<Permission> = mutableSetOf()

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH], mappedBy = "groups")
    var users = setOf<User>()

    @PostLoad
    @PostPersist
    private fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptText(id.toString())
    }
}