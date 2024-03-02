package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class DeleteUseCase {

    @Inject
    private lateinit var adRepository: AdRepository

    fun execute(adId: Int): Boolean {
        val activeReservations = adRepository.findActiveReservationsForAd(adId)

        return if (activeReservations.isEmpty()) {
            adRepository.delete(adId)
            true
        } else {
            false
        }
    }
}