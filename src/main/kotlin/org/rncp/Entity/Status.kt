package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Status(
        @Id
        var status_id: Int,
        var value: String,
) : PanacheEntityBase() {
    constructor() : this(0, "")
}

