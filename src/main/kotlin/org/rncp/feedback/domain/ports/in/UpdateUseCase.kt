package org.rncp.feedback.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.out.FeedbackRepository
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class UpdateUseCase {
    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    fun execute(feedbackId: Int, feedback: Feedback) {
        feedbackRepository.update(feedbackId, feedback)
    }
}