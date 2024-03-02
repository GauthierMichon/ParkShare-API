package org.rncp.reservation.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository
import org.rncp.feedback.domain.model.Feedback
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class GetByIdUseCase {

    @Inject
    private lateinit var reservationRepository: ReservationRepository

    fun execute(id: Int): Reservation? {
        return reservationRepository.getById(id)
    }
}