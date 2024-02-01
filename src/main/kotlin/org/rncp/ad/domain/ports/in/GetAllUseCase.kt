package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class GetAllUseCase {

    @Inject
    private lateinit var adRepository: AdRepository

    fun execute(): List<Ad> {
        return adRepository.getAll()
    }
}