package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class UpdateUseCase {

    @Inject
    lateinit var adRepository: AdRepository

    fun execute(adId: Int, updatedAd: Ad /*Changer pour un Ad nullable*/) {
        adRepository.updateAd(adId, updatedAd)
    }
}