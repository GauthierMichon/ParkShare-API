package org.rncp.reservation.infra.api

import kotlinx.serialization.Serializable
import org.rncp.LocalDateTimeSerializer
import org.rncp.reservation.domain.model.Reservation
import java.time.LocalDateTime

@Serializable
data class ReservationDTO(
        var id: Int? = null,
        var adId: Int,
        var userId: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        var beginDate: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class)
        var endDate: LocalDateTime,
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