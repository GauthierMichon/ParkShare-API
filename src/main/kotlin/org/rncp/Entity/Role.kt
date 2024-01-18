package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Role(
        @Id
        var role_id: Int,
        var name: String,
) : PanacheEntityBase() {
    constructor() : this(0, "")
}
