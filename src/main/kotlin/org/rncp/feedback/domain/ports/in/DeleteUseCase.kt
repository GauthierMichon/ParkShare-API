package org.rncp.feedback.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.feedback.domain.ports.out.FeedbackRepository

@ApplicationScoped
class DeleteUseCase {
    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    fun execute(adId: Int) {
        feedbackRepository.delete(adId)
    }
}