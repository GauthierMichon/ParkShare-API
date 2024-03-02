package org.rncp.reservation.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.infra.db.AdDao
import org.rncp.reservation.domain.model.Reservation
import org.rncp.status.infra.db.StatusDAO
import org.rncp.user.infra.db.UserDAO
import java.time.LocalDateTime

@Entity
@Table(name="reservation")
data class ReservationDAO(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @ManyToOne(targetEntity = AdDao::class, fetch = FetchType.LAZY)
        @JoinColumn(name="ad")
        var ad: AdDao,
        @ManyToOne(targetEntity = UserDAO::class, fetch = FetchType.LAZY)
        @JoinColumn(name="userId")
        var user: UserDAO,
        var beginDate: LocalDateTime,
        var endDate: LocalDateTime,
        var totalPrice: Double,
        @ManyToOne(targetEntity = StatusDAO::class, fetch = FetchType.LAZY)
        @JoinColumn(name="status")
        var status: StatusDAO,
) : PanacheEntityBase() {
    constructor() : this(0, AdDao(), UserDAO(), LocalDateTime.now(), LocalDateTime.now(), 0.0, StatusDAO())

    fun toReservation(): Reservation {
        return Reservation(id, ad.id!!, user.uid, beginDate, endDate, totalPrice, status.id!!)
    }
}