package org.rncp.reservation.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class DeleteUseCase {
    @Inject
    private lateinit var reservationRepository: ReservationRepository
    fun execute(adId: Int) {
        reservationRepository.delete(adId)
    }
}