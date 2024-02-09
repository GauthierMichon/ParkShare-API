package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.domain.model.Ad
import org.rncp.reservation.infra.db.ReservationDAO

@Entity
@Table(name = "ad")
data class AdDao(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var userId: String,
        var name: String,
        var description: String,
        var hourPrice: Double,
        var latitude: Double,
        var longitude: Double,
        var state: Boolean,
) : PanacheEntityBase() {
    constructor() : this(0, "", "", "", 0.0, 0.0, 0.0, false)

    fun toAd(): Ad {
        return Ad(id, userId, name, description, hourPrice, latitude, longitude, state)
    }
}

// Builder fromAd companion object Ad -> AdDao
