package org.rncp.image.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.image.domain.model.Image
import org.rncp.image.domain.ports.out.ImageRepository

@ApplicationScoped
class CreateUseCase {

    @Inject
    lateinit var imageRepository: ImageRepository

    fun execute(image: Image): Image {
        return imageRepository.create(image)
    }
}