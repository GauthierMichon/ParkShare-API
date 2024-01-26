package org.rncp.reservation.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.infra.db.AdDao
import org.rncp.reservation.domain.model.Reservation

@Entity
@Table(name="reservation")
data class ReservationDAO(
        @Id @GeneratedValue
        var id: Int? = null,
        @ManyToOne(targetEntity = AdDao::class, fetch = FetchType.LAZY)
        @JoinColumn(name="ad")
        var ad: AdDao,
        var userId: String,
        var beginDate: String,
        var endDate: String,
        var statusId: Int,
) : PanacheEntityBase() {
    constructor() : this(0, AdDao(), "", "", "", 0)

    fun toReservation(): Reservation {
        return Reservation(id, ad.id!!, userId, beginDate, endDate, statusId)
    }
}