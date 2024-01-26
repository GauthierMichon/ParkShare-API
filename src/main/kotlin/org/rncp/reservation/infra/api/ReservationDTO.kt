package org.rncp.reservation.infra.api

import kotlinx.serialization.Serializable
import org.rncp.reservation.domain.model.Reservation

@Serializable
data class ReservationDTO(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        var beginDate: String,
        var endDate: String,
        var statusId: Int,
) {
    companion object {
        fun fromReservation(reservation: Reservation): ReservationDTO {
            return ReservationDTO(
                    id = reservation.id,
                    adId = reservation.adId,
                    userId = reservation.userId,
                    beginDate = reservation.beginDate,
                    endDate = reservation.endDate,
                    statusId = reservation.statusId
            )
        }
    }
}