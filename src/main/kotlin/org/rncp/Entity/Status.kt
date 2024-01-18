package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
data class Status(
        var status_id: Int,
        var value: String,
) : PanacheEntity() {
    constructor() : this(0, "")
}

