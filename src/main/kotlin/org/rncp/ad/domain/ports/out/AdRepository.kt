package org.rncp.ad.domain.ports.out

import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.infra.api.AdDto
import org.rncp.ad.infra.db.AdDao
import org.rncp.reservation.domain.model.Reservation

interface AdRepository {
    fun createAd(ad: Ad)
    fun getAllAds(): List<Ad>
    fun getAdById(id: Int): Ad
    fun updateAd(adId: Int, updatedAd: Ad)
    fun deleteAd(adId: Int)
    fun findActiveReservationsForAd(adId: Int): List<Reservation>
    fun publish(adId: Int)
    fun unpublish(adId: Int)
}