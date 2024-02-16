package org.rncp.feedback.infra.api

import kotlinx.serialization.Serializable
import org.rncp.LocalDateTimeSerializer
import org.rncp.feedback.domain.model.Feedback
import java.time.LocalDateTime

@Serializable
class FeedbackCreateOrUpdateDTO(
        var adId: Int,
        var rating: Int?,
        var description: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        var date: LocalDateTime,
) {
    companion object {
        fun fromFeedback(feedback: Feedback): FeedbackCreateOrUpdateDTO {
            return FeedbackCreateOrUpdateDTO(
                    adId = feedback.adId,
                    rating = feedback.rating,
                    description = feedback.description,
                    date = feedback.date
            )
        }
    }
}