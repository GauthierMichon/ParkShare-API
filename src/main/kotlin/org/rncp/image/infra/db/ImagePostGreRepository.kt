package org.rncp.image.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.infra.db.AdPostGreRepository
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.out.FeedbackRepository
import org.rncp.image.domain.model.Image
import org.rncp.image.domain.ports.out.ImageRepository
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository
import org.rncp.user.infra.db.UserDAO
import org.rncp.user.infra.db.UserPostGreRepository

@ApplicationScoped
class ImagePostGreRepository : PanacheRepositoryBase<ImageDAO, Int> , ImageRepository {

    @Inject
    private lateinit var adRepository: AdPostGreRepository

    override fun create(image: Image): Image {
        val ad = adRepository.findById(image.adId)
        val imageDAO = ImageDAO(null, ad, image.imageData)
        persistAndFlush(imageDAO)
        return imageDAO.toImage()
    }

    override fun getById(id: Int): Image? {
        val imageDao = findById(id) ?: return null
        return imageDao.toImage()
    }

    override fun getListByAd(adId: Int): List<Image> {
        return list("ad.id", adId).map { it.toImage() }
    }

    override fun delete(imageId: Int) {
        deleteById(imageId)
    }
}