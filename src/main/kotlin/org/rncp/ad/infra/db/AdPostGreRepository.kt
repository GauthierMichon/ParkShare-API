package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.rncp.Entity.Reservation
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository
import jakarta.ws.rs.core.Response

@ApplicationScoped
class AdPostGreRepository : PanacheRepositoryBase<AdDao, Int>, AdRepository {
    override fun createAd(ad: Ad) {
        val adDao = AdDao(ad.ad_id, ad.user_id, ad.name, ad.description, ad.hour_price, ad.latitude, ad.longitude, ad.state)
        persist(adDao)
    }

    override fun getAllAds(): List<Ad> {
        return listAll().map { it.toAd() }
    }

    override fun getAdById(id: Int): Ad {
        return findById(id).toAd()
    }
    @Transactional
    override fun patchAd(adId: Int, updatedAd: Ad): Response {
        val existingAd = findById(adId)

        existingAd.apply {
            name = updatedAd.name ?: name
            description = updatedAd.description ?: description
            hour_price = updatedAd.hour_price ?: hour_price
            latitude = updatedAd.latitude ?: latitude
            longitude = updatedAd.longitude ?: longitude
            state = updatedAd.state ?: state
        }
        persistAndFlush(existingAd)
        return Response.ok(existingAd).build()
    }

    override fun deleteAd(adId: Int) {
        deleteById(adId)
    }

    override fun findActiveReservationsForAd(adId: Int): List<Reservation> {
        // Implémentez la logique pour récupérer les réservations actives pour une annonce
        // Vous pouvez utiliser des requêtes JPA ou d'autres méthodes selon votre base de données
        // Retournez la liste des réservations actives
        return emptyList()
        //return mutableListOf<Reservation>(Reservation())
    }
}
