package org.rncp.reservation.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.infra.db.AdPostGreRepository
import org.rncp.feedback.domain.model.Feedback
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.out.ReservationRepository
import org.rncp.status.infra.db.StatusPostGreRepository
import org.rncp.user.infra.db.UserDAO
import org.rncp.user.infra.db.UserPostGreRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.Duration

@ApplicationScoped
class ReservationPostGreRepository : PanacheRepositoryBase<ReservationDAO, Int> , ReservationRepository {

    @Inject
    private lateinit var adRepository: AdPostGreRepository

    @Inject
    private lateinit var statusRepository: StatusPostGreRepository

    @Inject
    private lateinit var userRepository: UserPostGreRepository

    override fun create(reservation: Reservation): Reservation {
        val ad = adRepository.findById(reservation.adId)
        val status = statusRepository.findById(reservation.statusId)
        val user = userRepository.find("uid", reservation.userId).firstResult<UserDAO>()
        val timeReservation = Duration.between(reservation.beginDate, reservation.endDate).toMinutes() / 60.0
        val totalPrice = BigDecimal(ad.hourPrice * timeReservation).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        val reservationDAO = ReservationDAO(null, ad, user, reservation.beginDate, reservation.endDate, totalPrice, status)
        persistAndFlush(reservationDAO)
        return reservationDAO.toReservation()
    }
    override fun getById(id: Int): Reservation? {
        val reservationDao = findById(id) ?: return null
        return reservationDao.toReservation()
    }
    override fun getListByAd(adId: Int): List<Reservation> {
        return list("ad.id", adId).map { it.toReservation() }
    }

    override fun getListByStatus(statusId: Int): List<Reservation> {
        return list("status.id", statusId).map { it.toReservation() }
    }

    override fun accept(reservation: Reservation) {
        val reservationDao = findById(reservation.id)
        reservationDao.apply {
            status = statusRepository.findById(1)
        }
        persistAndFlush(reservationDao)
    }

    override fun cancel(reservation: Reservation) {
        val reservationDao = findById(reservation.id)
        reservationDao.apply {
            status = statusRepository.findById(3)
        }
        persistAndFlush(reservationDao)
    }

    override fun delete(id: Int) {
        deleteById(id)
    }
    override fun update(reservation: Reservation) {
        val reservationToUpdate = findById(reservation.id)
        reservationToUpdate.apply {
            ad = adRepository.findById(reservation.adId)
            user = userRepository.find("uid", reservation.userId).firstResult()
            beginDate = reservation.beginDate
            endDate = reservation.endDate
            val timeReservation = Duration.between(beginDate, endDate).toMinutes() / 60.0
            val timeReservationRound = "%.2f".format(timeReservation).replace(",", ".").toDouble()
            totalPrice = ad.hourPrice * timeReservationRound
            status = reservationToUpdate.status
        }
        persistAndFlush(reservationToUpdate)
    }
}