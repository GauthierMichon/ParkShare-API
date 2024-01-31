package org.rncp.feedback.infra.api

import kotlinx.serialization.Serializable
import org.rncp.feedback.domain.model.Feedback

@Serializable
data class FeedbackDTO(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        var rating: Int?,
        var description: String,
        var date: String,
) {
    companion object {
        fun fromFeedback(feedback: Feedback): FeedbackDTO {
            return FeedbackDTO(
                    id = feedback.id,
                    adId = feedback.adId,
                    userId = feedback.userId,
                    rating = feedback.rating,
                    description = feedback.description,
                    date = feedback.date
            )
        }
    }
}