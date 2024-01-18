package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
data class Reservation(
        var reservation_id: Int,
        var annoncement_id: Int,
        var user_id: Int,
        var begin_date: String,
        var end_date: String,
        var status_id: Int,
) : PanacheEntity() {
    constructor() : this(0, 0, 0, "", "", 0)
}

