package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class DeleteUseCase {

    @Inject
    lateinit var adRepository: AdRepository

    fun execute(adId: Int) {
        val activeReservations = adRepository.findActiveReservationsForAd(adId)

        if (activeReservations.isEmpty()) {
            adRepository.deleteAd(adId)
        } else {
        }
    }
}