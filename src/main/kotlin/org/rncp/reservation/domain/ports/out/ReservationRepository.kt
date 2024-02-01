package org.rncp.reservation.domain.ports.out

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list
import org.rncp.reservation.domain.model.Reservation

interface ReservationRepository {
    fun create(reservation: Reservation)
    fun getListByAd(adId: Int): List<Reservation>
    fun delete(adId: Int)
    fun update(reservationId: Int, reservation: Reservation)
}