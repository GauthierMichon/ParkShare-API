package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.domain.model.Ad
import org.rncp.reservation.infra.db.ReservationDAO
import org.rncp.user.infra.db.UserDAO

@Entity
@Table(name = "ad")
data class AdDao(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @ManyToOne(targetEntity = UserDAO::class, fetch = FetchType.LAZY)
        @JoinColumn(name="userId")
        var user: UserDAO,
        var name: String,
        var description: String,
        var hourPrice: Float,
        var latitude: Float,
        var longitude: Float,
        var state: Boolean,
) : PanacheEntityBase() {
    constructor() : this(0, UserDAO(), "", "", 0.0f, 0.0f, 0.0f, false)

    fun toAd(): Ad {
        return Ad(id, user.uid, name, description, hourPrice, latitude, longitude, state)
    }
}

// Builder fromAd companion object Ad -> AdDao
