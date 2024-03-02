package org.rncp.feedback.infra.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.rncp.LocalDateTimeSerializer
import org.rncp.feedback.domain.model.Feedback
import java.time.LocalDateTime

@Serializable
data class FeedbackDTO(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        var rating: Int?,
        var description: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        var date: LocalDateTime,
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