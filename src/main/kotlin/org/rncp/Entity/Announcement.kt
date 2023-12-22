package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
data class Announcement(
        var commentaire: String? = null
) : PanacheEntity()
