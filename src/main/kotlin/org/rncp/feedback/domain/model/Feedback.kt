package org.rncp.feedback.domain.model

data class Feedback(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        var rating: Int?,
        var description: String,
        var date: String,
) {
    constructor() : this(0, 0, "", 0, "", "")
}
