package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Note(
        @Id
        var note_id: Int,
        var annoncement_id: Int,
        var user_id: Int,
        var value: Int,
) : PanacheEntityBase() {
    constructor() : this(0, 0, 0, 0)
}
