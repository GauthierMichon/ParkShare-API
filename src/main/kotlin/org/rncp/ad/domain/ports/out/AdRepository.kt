package org.rncp.ad.domain.ports.out

import org.rncp.ad.domain.model.Ad
import org.rncp.reservation.domain.model.Reservation

interface AdRepository {
    fun create(ad: Ad): Ad
    fun getAll(): List<Ad>
    fun getById(id: Int): Ad?
    fun update(adData: Ad): Ad
    fun delete(adId: Int)
    fun findActiveReservationsForAd(adId: Int): List<Reservation>
    fun publish(adId: Int)
    fun unpublish(adId: Int)
}