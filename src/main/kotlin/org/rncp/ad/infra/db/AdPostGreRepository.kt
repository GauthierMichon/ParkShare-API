package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.out.AdRepository
import org.rncp.feedback.infra.db.FeedbackPostGreRepository
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.`in`.GetListByAdUseCase
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class AdPostGreRepository : PanacheRepositoryBase<AdDao, Int>, AdRepository {

    @Inject
    lateinit var feedbackRepository: FeedbackPostGreRepository

    @Inject
    lateinit var reservationRepository: ReservationRepository

    override fun createAd(ad: Ad) {
        val adDao = AdDao(null, ad.userId, ad.name, ad.description, ad.hourPrice, ad.latitude, ad.longitude, ad.state)
        persist(adDao)
    }

    override fun getAllAds(): List<Ad> {
        return listAll().map { it.toAd() }
    }

    override fun getAdById(id: Int): Ad {
        return findById(id).toAd()
    }

    override fun updateAd(adId: Int, updatedAd: Ad) {
        val existingAd = findById(adId)

        existingAd.apply {
            name = updatedAd.name
            description = updatedAd.description
            hourPrice = updatedAd.hourPrice
            latitude = updatedAd.latitude
            longitude = updatedAd.longitude
            state = updatedAd.state
        }
        persistAndFlush(existingAd)
    }



    override fun deleteAd(adId: Int) {
        var feedbacks = feedbackRepository.getListByAd(adId)
        feedbacks.map { feedback ->
            feedbackRepository.deleteById(feedback.id)
        }
        var reservations = reservationRepository.getListByAd(adId)
        reservations.map { reservation ->
            reservationRepository.delete(reservation.id!!)
        }
        deleteById(adId)
    }

    override fun findActiveReservationsForAd(adId: Int): List<Reservation> {
        // TODO : Implémentez la logique pour récupérer les réservations actives pour une annonce
        // Vous pouvez utiliser des requêtes JPA ou d'autres méthodes selon votre base de données
        // Retournez la liste des réservations actives
        return emptyList()
        //return reservationGetListByAdUseCase.execute(adId)
        //return mutableListOf<Reservation>(Reservation())
    }

    override fun publish(adId: Int) {
        val adDao = findById(adId)
        adDao.state = true
        persistAndFlush(adDao)
    }

    override fun unpublish(adId: Int) {
        val adDao = findById(adId)
        adDao.state = false
        persistAndFlush(adDao)
    }
}
