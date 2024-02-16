package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class UpdateUseCase {

    @Inject
    private lateinit var adRepository: AdRepository

    fun execute(adId: Int, adData: Ad): Response {
        val ad = adRepository.getById(adId)
        return if (ad != null) {
            adRepository.update(adData)
            Response.status(Response.Status.NO_CONTENT).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }

    }
}