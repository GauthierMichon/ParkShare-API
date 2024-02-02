package org.rncp.ad.domain.ports.out

import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.infra.api.AdDto
import org.rncp.ad.infra.db.AdDao
import org.rncp.reservation.domain.model.Reservation

interface AdRepository {
    fun create(ad: Ad): Ad
    fun getAll(): List<Ad>
    fun getById(id: Int): Ad?
    fun update(adId: Int, adData: Ad): Ad
    fun delete(adId: Int)
    fun findActiveReservationsForAd(adId: Int): List<Reservation>
    fun publish(adId: Int)
    fun unpublish(adId: Int)
}