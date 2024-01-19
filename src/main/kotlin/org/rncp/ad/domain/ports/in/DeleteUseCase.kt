package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class DeleteUseCase {

    @Inject
    lateinit var adRepository: AdRepository

    fun deleteAd(adId: Int) {
        val activeReservations = adRepository.findActiveReservationsForAd(adId)

        if (activeReservations.isEmpty()) {
            // Aucune réservation active, supprimer l'annonce
            adRepository.deleteAd(adId)
        } else {
            // Des réservations actives existent, gérer la logique en conséquence
            // Vous pouvez choisir de lever une exception, de renvoyer un code d'erreur, etc.
            // En fonction de la logique de votre application
            // Par exemple, vous pouvez créer une exception spécifique pour cette situation
        }
    }
}