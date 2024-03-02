package org.rncp.reservation.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository
import org.rncp.status.domain.ports.out.StatusRepository

@ApplicationScoped
class CancelUseCase {

    @Inject
    private lateinit var reservationRepository: ReservationRepository

    fun execute(reservationId: Int): Response {
        val reservation = reservationRepository.getById(reservationId)
        return if (reservation != null) {
            reservation.statusId = 3
            reservationRepository.cancel(reservation)
            Response.ok().build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}