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
    private lateinit var feedbackRepository: FeedbackPostGreRepository

    @Inject
    private lateinit var reservationRepository: ReservationRepository

    override fun create(ad: Ad): Ad {
        val adDao = AdDao(null, ad.userId, ad.name, ad.description, ad.hourPrice, ad.latitude, ad.longitude, ad.state)
        persist(adDao)
        return adDao.toAd()
    }

    override fun getAll(): List<Ad> {
        return listAll().map { it.toAd() }
    }

    override fun getById(id: Int): Ad? {
        val adDao = findById(id) ?: return null
        return adDao.toAd()
    }

    override fun update(adId: Int, adData: Ad): Ad {
        val adDao = findById(adId)

        adDao.apply {
            name = adData.name
            description = adData.description
            hourPrice = adData.hourPrice
            latitude = adData.latitude
            longitude = adData.longitude
            state = adData.state
        }
        persistAndFlush(adDao)
        return adDao.toAd()
    }



    override fun delete(adId: Int) {
        val feedbacks = feedbackRepository.getListByAd(adId)
        feedbacks.map { feedback ->
            feedbackRepository.deleteById(feedback.id)
        }
        val reservations = reservationRepository.getListByAd(adId)
        reservations.map { reservation ->
            reservationRepository.delete(reservation.id!!)
        }
        deleteById(adId)
    }

    override fun findActiveReservationsForAd(adId: Int): List<Reservation> {
        return reservationRepository.getListByAd(adId)
    }

    override fun save(ad: Ad) {
        val adDao = findById(ad.id)

        adDao.apply {
            state = ad.state
        }
        persistAndFlush(adDao)
    }
}
