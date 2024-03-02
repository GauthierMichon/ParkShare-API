package org.rncp.reservation.domain.model

import java.time.LocalDateTime

data class Reservation(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        var beginDate: LocalDateTime,
        var endDate: LocalDateTime,
        var totalPrice: Double?,
        var statusId: Int,
)  {
    constructor() : this(0, 0, "", LocalDateTime.now(), LocalDateTime.now(), 0.0, 0)
}
