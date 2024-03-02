package org.rncp.image.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.out.FeedbackRepository
import org.rncp.image.domain.model.Image
import org.rncp.image.domain.ports.out.ImageRepository

@ApplicationScoped
class GetListByAdUseCase {
    @Inject
    lateinit var imageRepository: ImageRepository

    fun execute(adId: Int): List<Image> {
        return imageRepository.getListByAd(adId)
    }
}