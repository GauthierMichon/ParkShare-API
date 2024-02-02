package org.rncp.feedback.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.infra.db.AdPostGreRepository
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.out.FeedbackRepository
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository

@ApplicationScoped
class FeedbackPostGreRepository : PanacheRepositoryBase<FeedbackDAO, Int> , FeedbackRepository {

    @Inject
    lateinit var adRepository: AdPostGreRepository

    override fun create(feedback: Feedback): Feedback {
        val ad = adRepository.findById(feedback.adId)
        val feedbackDAO = FeedbackDAO(null, ad, feedback.userId, feedback.rating, feedback.description, feedback.date)
        persistAndFlush(feedbackDAO)
        return feedbackDAO.toFeedback()
    }
    override fun getById(id: Int): Feedback? {
        val feedbackDao = findById(id) ?: return null
        return feedbackDao.toFeedback()
    }
    override fun getListByAd(adId: Int): List<Feedback> {
        return list("ad.id", adId).map { it.toFeedback() }
    }
    override fun update(feedbackId: Int, feedback: Feedback) {
        val feedbackToUpdate = findById(feedbackId)
        feedbackToUpdate.apply {
            ad = adRepository.findById(feedback.adId)
            userId = feedback.userId
            rating = feedback.rating
            description = feedback.description
            date = feedback.date
        }
        persistAndFlush(feedbackToUpdate)
    }

    override fun delete(feedbackId: Int) {
        deleteById(feedbackId)
    }
}