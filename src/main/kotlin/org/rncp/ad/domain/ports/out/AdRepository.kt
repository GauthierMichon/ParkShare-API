package org.rncp.ad.domain.ports.out

import jakarta.ws.rs.core.Response
import org.rncp.Entity.Reservation
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.infra.db.AdDao

interface AdRepository {
    fun createAd(ad: Ad)
    fun getAllAds(): List<Ad>
    fun getAdById(id: Int): Ad
    fun patchAd(adId: Int, updatedAd: Ad): Response
    fun deleteAd(adId: Int)
    fun findActiveReservationsForAd(adId: Int): List<Reservation>
}