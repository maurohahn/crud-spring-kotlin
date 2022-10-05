package me.maurohahn.crudapi.entity

import me.maurohahn.crudapi.util.crypto.CryptoProvider
import javax.persistence.*

@Entity
@Table(name = "permissions")
class Permission : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dp_erp_permissions_seq")
    @SequenceGenerator(name = "dp_erp_permissions_seq", sequenceName = "dp_erp_permissions_seq", allocationSize = 1)
    override var id: Long? = null

    @Column(name = "permission_name", unique = true)
    var permissionName: String? = null

    @Column(name = "is_active")
    var isActive: Boolean? = null

    @Column(name = "is_default")
    var isDefault: Boolean = false

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH], mappedBy = "permissions")
    var groups: MutableSet<Group> = mutableSetOf()

    @PostLoad
    @PostPersist
    private fun onGetFromDB() {
        encryptedId = CryptoProvider.encryptText(id.toString())
    }
}