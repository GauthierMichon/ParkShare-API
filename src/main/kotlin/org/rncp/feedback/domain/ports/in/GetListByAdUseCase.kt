package org.rncp.feedback.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.out.FeedbackRepository

@ApplicationScoped
class GetListByAdUseCase {
    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    fun execute(adId: Int): List<Feedback> {
        return feedbackRepository.getListByAd(adId)
    }
}