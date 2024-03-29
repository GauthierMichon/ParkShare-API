package org.rncp.reservation.domain.ports.out

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list
import org.rncp.ad.domain.model.Ad
import org.rncp.reservation.domain.model.Reservation

interface ReservationRepository {
    fun create(reservation: Reservation): Reservation
    fun getById(id: Int): Reservation?
    fun getListByAd(adId: Int): List<Reservation>
    fun getListByUser(userId: String): List<Reservation>
    fun accept(reservation: Reservation)
    fun cancel(reservation: Reservation)
    fun delete(id: Int)
    fun update(reservation: Reservation)
}