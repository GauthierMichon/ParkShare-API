package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
data class Role(
        var role_id: Int = 0,
        var name: String = "",
) : PanacheEntity()
