package org.rncp.feedback.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.out.FeedbackRepository

@ApplicationScoped
class GetByIdUseCase {

    @Inject
    private lateinit var feedbackRepository: FeedbackRepository

    fun execute(id: Int): Feedback? {
        return feedbackRepository.getById(id)
    }
}