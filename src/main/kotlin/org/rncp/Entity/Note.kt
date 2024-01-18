package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
data class Note(
        var note_id: Int,
        var annoncement_id: Int,
        var user_id: Int,
        var value: Int,
) : PanacheEntity() {
    constructor() : this(0, 0, 0, 0)
}
