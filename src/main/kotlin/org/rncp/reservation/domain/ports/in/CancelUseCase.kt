package org.rncp.reservation.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository
import org.rncp.status.domain.ports.out.StatusRepository

@ApplicationScoped
class CancelUseCase {

    @Inject
    private lateinit var reservationRepository: ReservationRepository

    fun execute(reservationId: Int) {
        val reservation = reservationRepository.getById(reservationId)
        if (reservation != null) {
            reservation.statusId = 3
            reservationRepository.cancel(reservation)
        }
    }
}