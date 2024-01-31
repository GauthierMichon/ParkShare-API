package org.rncp.reservation.domain.model

data class Reservation(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        var beginDate: String,
        var endDate: String,
        var statusId: Int,
)  {
    constructor() : this(0, 0, "", "", "", 0)
}
