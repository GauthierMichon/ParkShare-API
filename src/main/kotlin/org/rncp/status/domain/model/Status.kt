package org.rncp.status.domain.model

import io.quarkus.hibernate.orm.panache.PanacheEntityBase

data class Status(
        val id: Int? = null,
        val label: String,
): PanacheEntityBase() {
    constructor(): this(null, "")
}
