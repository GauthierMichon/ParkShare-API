package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import org.rncp.Entity.Reservation
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository

@ApplicationScoped
class AdPostGreRepository : PanacheRepositoryBase<AdDao, Int>, AdRepository {
    override fun createAd(ad: Ad) {
        val adDao = AdDao(ad.ad_id, ad.user_id, ad.name, ad.description, ad.hour_price, ad.latitude, ad.longitude)
        persist(adDao)
    }

    override fun getAllAds() {
        TODO("Not yet implemented")
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
