package org.rncp.ad.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class GetAllUseCase {

    @Inject
    private lateinit var adRepository: AdRepository

    fun execute(latitude: Double, longitude: Double, maxDistanceKm: Double): List<Ad> {
        val allAds =  adRepository.getAll()

        return if (latitude != 0.0 && longitude != 0.0 && maxDistanceKm != 0.0) {
            allAds.filter { ad ->
                val distance = calculateDistance(latitude, longitude, ad.latitude, ad.longitude)
                distance <= maxDistanceKm
            }
        } else {
            allAds
        }

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



}