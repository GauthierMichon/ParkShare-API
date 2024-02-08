package org.rncp.reservation.infra.api

import kotlinx.serialization.Serializable
import org.rncp.LocalDateTimeSerializer
import org.rncp.reservation.domain.model.Reservation
import java.time.LocalDateTime

@Serializable
class ReservationCreateOrUpdateDTO(
        var adId: Int,
        @Serializable(with = LocalDateTimeSerializer::class)
        var beginDate: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class)
        var endDate: LocalDateTime,
        var statusId: Int,
) {
    companion object {
        fun fromReservation(reservation: Reservation): ReservationCreateOrUpdateDTO {
            return ReservationCreateOrUpdateDTO(
                    adId = reservation.adId,
                    beginDate = reservation.beginDate,
                    endDate = reservation.endDate,
                    statusId = reservation.statusId
            )
        }
    }
}