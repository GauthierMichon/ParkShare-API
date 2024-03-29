package org.rncp.reservation.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class GetListByAdUseCase {
    @Inject
    lateinit var reservationRepository: ReservationRepository

    fun execute(adId: Int, statusId: Int?): List<Reservation> {
        val allReservations = reservationRepository.getListByAd(adId)

        return if (statusId != null) {
            allReservations.filter { it.statusId == statusId }
        } else {
            allReservations
        }
    }
}