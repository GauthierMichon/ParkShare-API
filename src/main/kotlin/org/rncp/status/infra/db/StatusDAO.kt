package org.rncp.status.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.domain.model.Ad
import org.rncp.status.domain.model.Status

@Entity
@Table(name="status")
data class StatusDAO(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val label: String,
): PanacheEntityBase() {
    constructor(): this(null, "")

    fun toStatus(): Status {
        return Status(id, label)
    }
}
