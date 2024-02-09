package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.model.SortField
import org.rncp.ad.domain.model.SortType
import org.rncp.ad.domain.ports.out.AdRepository
import org.rncp.feedback.domain.ports.out.FeedbackRepository
import org.rncp.reservation.domain.ports.out.ReservationRepository
import java.time.LocalDateTime

@ApplicationScoped
class GetAllUseCase {

    @Inject
    private lateinit var adRepository: AdRepository

    @Inject
    private lateinit var reservationRepository: ReservationRepository

    @Inject
    private lateinit var feedbackRepository: FeedbackRepository

    fun execute(
            latitude: Double?,
            longitude: Double?,
            maxDistanceKm: Double?,
            beginDate: String?,
            endDate: String?,
            minRate: Double?,
            maxHourPrice: Double?,
            sortField: SortField?,
            sortType: SortType?): List<Ad> {
        val allAds =  adRepository.getAll()
        var filteredAds = allAds

        if (latitude != null && longitude != null && maxDistanceKm != null) {
            filteredAds = filteredAds.filter { ad ->
                val distance = calculateDistance(latitude, longitude, ad.latitude, ad.longitude)
                distance <= maxDistanceKm
            }
        }

        if (beginDate != null && endDate != null) {
            filteredAds = filteredAds.filter { ad ->
                isAdAvailable(ad, LocalDateTime.parse(beginDate), LocalDateTime.parse(endDate))
            }
        }

        if (minRate != null) {
            filteredAds = filteredAds.filter { ad ->
                val feedbacks = feedbackRepository.getListByAd(ad.id!!)
                val averageRating = feedbacks.mapNotNull { it.rating }.average()

                averageRating >= minRate
            }
        }

        if (maxHourPrice != null) {
            filteredAds = filteredAds.filter { ad ->
                ad.hourPrice <= maxHourPrice
            }
        }

        // Trier les annonces
        if (sortField != null && sortType != null) {
            filteredAds = when (sortField) {
                SortField.HOUR_PRICE -> sortAdsByHourPrice(filteredAds, sortType)
                SortField.RATING -> sortAdsByRating(filteredAds, sortType)
            }
        }

        return filteredAds
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Rayon moyen de la Terre en kilomètres

        // Conversion des latitudes et longitudes de degrés à radians
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        // Différences entre les latitudes et les longitudes
        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        // Formule de Haversine
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        // Distance en kilomètres
        val distance = R * c

        return distance
    }

    private fun isAdAvailable(ad: Ad, beginDate: LocalDateTime?, endDate: LocalDateTime?): Boolean {
        // Filtrer les réservations pour cette annonce
        val reservations = ad.id?.let { reservationRepository.getListByAd(it) }

        // Si la période n'est pas spécifiée, l'annonce est toujours considérée comme disponible
        if (beginDate == null || endDate == null) {
            return true
        }

        // Vérifier si une réservation chevauche la période spécifiée
        if (reservations != null) {
            if (reservations.any { reservation ->
                        reservation.endDate.isAfter(beginDate) && reservation.beginDate.isBefore(endDate)
                    }) {
                // Il y a une réservation qui chevauche la période spécifiée
                return false
            }

            if (reservations.any { reservation ->
                        endDate.isAfter(reservation.beginDate) && beginDate.isBefore(reservation.endDate)
                    }) {
                // La période spécifiée chevauche une réservation existante
                return false
            }
        }

        // Aucun chevauchement trouvé, l'annonce est disponible
        return true
    }

    private fun sortAdsByHourPrice(ads: List<Ad>, sortType: SortType): List<Ad> {
        return when (sortType) {
            SortType.ASC -> ads.sortedBy { it.hourPrice }
            SortType.DESC -> ads.sortedByDescending { it.hourPrice }
        }
    }

    private fun sortAdsByRating(ads: List<Ad>, sortType: SortType): List<Ad> {
        return when (sortType) {
            SortType.ASC -> ads.sortedBy { ad ->
                val feedbacks = feedbackRepository.getListByAd(ad.id!!)
                val averageRating = feedbacks.mapNotNull { it.rating }.average()
                averageRating ?: Double.MIN_VALUE
            }
            SortType.DESC -> ads.sortedByDescending { ad ->
                val feedbacks = feedbackRepository.getListByAd(ad.id!!)
                val averageRating = feedbacks.mapNotNull { it.rating }.average()
                averageRating ?: Double.MAX_VALUE
            }
        }
    }
}