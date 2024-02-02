package org.rncp.reservation.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.infra.db.AdDao
import org.rncp.reservation.domain.model.Reservation
import org.rncp.status.infra.db.StatusDAO
import java.time.LocalDateTime

@Entity
@Table(name="reservation")
data class ReservationDAO(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @ManyToOne(targetEntity = AdDao::class, fetch = FetchType.LAZY)
        @JoinColumn(name="ad")
        var ad: AdDao,
        var userId: String,
        var beginDate: LocalDateTime,
        var endDate: LocalDateTime,
        @ManyToOne(targetEntity = StatusDAO::class, fetch = FetchType.LAZY)
        @JoinColumn(name="status")
        var status: StatusDAO,
) : PanacheEntityBase() {
    constructor() : this(0, AdDao(), "", LocalDateTime.now(), LocalDateTime.now(), StatusDAO())

    fun toReservation(): Reservation {
        return Reservation(id, ad.id!!, userId, beginDate, endDate, status.id!!)
    }
}