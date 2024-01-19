package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.Dto.UserDto
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository
import org.rncp.ad.infra.api.AdDto

@ApplicationScoped
class GetAllUseCase {

    @Inject
    lateinit var adRepository: AdRepository

    fun execute(): List<Ad> {
        return adRepository.getAllAds()
    }
}