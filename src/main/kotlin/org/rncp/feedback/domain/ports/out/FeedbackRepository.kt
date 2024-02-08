package org.rncp.feedback.domain.ports.out

import org.rncp.ad.domain.model.Ad
import org.rncp.feedback.domain.model.Feedback

interface FeedbackRepository {
    fun create(feedback: Feedback): Feedback
    fun getById(id: Int): Feedback?
    fun getListByAd(adId: Int): List<Feedback>
    fun delete(feedbackId: Int)
    fun update(feedback: Feedback)
}