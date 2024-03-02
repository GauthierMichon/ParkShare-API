package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class PublishUseCase {
    @Inject
    private lateinit var adRepository: AdRepository

    fun execute(adId: Int): Response {
        val ad = adRepository.getById(adId)
        return if (ad != null) {
            ad.state = true
            adRepository.save(ad)
            Response.status(Response.Status.NO_CONTENT).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}