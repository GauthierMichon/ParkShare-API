package org.rncp.ad.domain.ports.out

import org.rncp.Entity.Reservation
import org.rncp.ad.domain.model.Ad

interface AdRepository {
    fun createAd(ad: Ad)
    fun getAllAds()
    fun deleteAd(adId: Int)
    fun findActiveReservationsForAd(adId: Int): List<Reservation>
}