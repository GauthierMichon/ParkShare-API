package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class GetAdByIdUseCase {

    @Inject
    private lateinit var adRepository: AdRepository

    fun execute(id: Int): Ad? {
        return adRepository.getById(id)
    }
}