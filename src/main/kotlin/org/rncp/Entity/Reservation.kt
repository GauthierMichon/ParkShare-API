package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Reservation(
        @Id
        var reservation_id: Int,
        var annoncement_id: Int,
        var user_id: Int,
        var begin_date: String,
        var end_date: String,
        var status_id: Int,
) : PanacheEntityBase() {
    constructor() : this(0, 0, 0, "", "", 0)
}

