package org.rncp.reservation.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.infra.db.AdPostGreRepository
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class ReservationPostGreRepository : PanacheRepositoryBase<ReservationDAO, Int> , ReservationRepository {

    @Inject
    lateinit var adRepository: AdPostGreRepository

    override fun create(reservation: Reservation) {
        val ad = adRepository.findById(reservation.adId)
        val reservationDAO = ReservationDAO(null, ad, reservation.userId, reservation.beginDate, reservation.endDate, reservation.statusId)
        persistAndFlush(reservationDAO)
    }
    override fun getListByAd(adId: Int): List<Reservation> {
        return list("ad.id", adId).map { it.toReservation() }
    }
    override fun delete(id: Int) {

    }
    override fun update(reservationId: Int, reservation: Reservation) {
        val reservationToUpdate = findById(reservationId)
        reservationToUpdate.apply {
            ad = adRepository.findById(reservation.adId)
            userId = reservation.userId
            beginDate = reservation.beginDate
            endDate = reservation.endDate
            statusId = reservation.statusId
        }
        persistAndFlush(reservationToUpdate)
    }
}