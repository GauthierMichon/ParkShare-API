package org.rncp.reservation.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.infra.db.AdPostGreRepository
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository
import org.rncp.status.infra.db.StatusPostGreRepository

@ApplicationScoped
class ReservationPostGreRepository : PanacheRepositoryBase<ReservationDAO, Int> , ReservationRepository {

    @Inject
    private lateinit var adRepository: AdPostGreRepository

    @Inject
    private lateinit var statusRepository: StatusPostGreRepository

    override fun create(reservation: Reservation) {
        val ad = adRepository.findById(reservation.adId)
        val status = statusRepository.findById(reservation.statusId)
        val reservationDAO = ReservationDAO(null, ad, reservation.userId, reservation.beginDate, reservation.endDate, status)
        persistAndFlush(reservationDAO)
    }
    override fun getListByAd(adId: Int): List<Reservation> {
        return list("ad.id", adId).map { it.toReservation() }
    }

    override fun getListByStatus(statusId: Int): List<Reservation> {
        return list("status.id", statusId).map { it.toReservation() }
    }

    override fun cancel(reservationId: Int): Reservation {
        val reservation = findById(reservationId)
        val status = statusRepository.findById(3)
        reservation.status = status
        persistAndFlush(reservation)
        return reservation.toReservation()
    }

    override fun delete(id: Int) {
        deleteById(id)
    }
    override fun update(reservationId: Int, reservation: Reservation) {
        val reservationToUpdate = findById(reservationId)
        reservationToUpdate.apply {
            ad = adRepository.findById(reservation.adId)
            userId = reservation.userId
            beginDate = reservation.beginDate
            endDate = reservation.endDate
            status = statusRepository.findById(reservation.statusId)
        }
        persistAndFlush(reservationToUpdate)
    }
}