package org.rncp.feedback.domain.ports.out

import org.rncp.feedback.domain.model.Feedback

interface FeedbackRepository {
    fun create(feedback: Feedback): Feedback
    fun getListByAd(feedbackId: Int): List<Feedback>
    fun delete(feedbackId: Int)
    fun update(feedbackId: Int, feedback: Feedback)
}