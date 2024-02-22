package org.rncp.image.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.image.domain.ports.out.ImageRepository

@ApplicationScoped
class DeleteUseCase {
    @Inject
    lateinit var imageRepository: ImageRepository

    fun execute(adId: Int) {
        imageRepository.delete(adId)
    }
}