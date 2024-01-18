package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Announcement(
        @Id
        var annoucement_id: Int,
        var user_id: Int,
        var name: String,
        var description: String,
        var hour_price: Float,
        var latitude: String,
        var longitude: String
) : PanacheEntityBase() {
    constructor() : this(0, 0, "", "", 0.0f, "", "")
}
