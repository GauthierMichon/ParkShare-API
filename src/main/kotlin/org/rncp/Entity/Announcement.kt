package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
data class Announcement(
        var annoucement_id: Int,
        var user_id: Int,
        var name: String,
        var description: String,
        var hour_price: Float,
        var latitude: String,
        var longitude: String
) : PanacheEntity() {
    constructor() : this(0, 0, "", "", 0.0f, "", "")
}
