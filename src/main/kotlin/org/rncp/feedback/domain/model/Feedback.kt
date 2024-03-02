package org.rncp.feedback.domain.model

import java.time.LocalDateTime
import java.util.Date

data class Feedback(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        var rating: Int?,
        var description: String,
        var date: LocalDateTime,
) {
    constructor() : this(0, 0, "", 0, "", LocalDateTime.now())
}
