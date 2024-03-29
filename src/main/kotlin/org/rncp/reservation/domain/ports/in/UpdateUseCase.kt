package org.rncp.reservation.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class UpdateUseCase {
    @Inject
    private lateinit var reservationRepository: ReservationRepository

    fun execute(reservationData: Reservation): Response {
        val reservation = reservationRepository.getById(reservationData.id!!)
        return if (reservation != null) {
            reservationRepository.update(reservationData)
            Response.status(Response.Status.NO_CONTENT).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}