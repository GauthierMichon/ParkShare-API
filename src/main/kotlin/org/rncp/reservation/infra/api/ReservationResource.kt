package org.rncp.reservation.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.rncp.ad.domain.ports.`in`.GetAdByIdUseCase
import org.rncp.ad.infra.api.AdDto
import org.rncp.feedback.infra.api.FeedbackDTO
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.`in`.*
import java.time.LocalDateTime


@Path("/api/reservation")
class ReservationResource {
    @Inject
    lateinit var createUseCase: CreateUseCase

    @Inject
    lateinit var getListByAdUseCase: GetListByAdUseCase

    @Inject
    lateinit var updateUseCase: UpdateUseCase

    @Inject
    lateinit var getListByStatusUseCase: GetListByStatusUseCase

    @Inject
    lateinit var deleteUseCase: DeleteUseCase

    @Inject
    lateinit var cancelUseCase: CancelUseCase

    @Inject
    lateinit var getOneUseCase: GetByIdUseCase

    @Inject
    lateinit var getAdbyIdUseCase: GetAdByIdUseCase


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") adId: Int): Response {
        val reservation = getOneUseCase.execute(adId)
        return if (reservation != null) {
            return Response.ok(ReservationDTO.fromReservation(reservation)).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun create(reservationDTO: ReservationDTO): Response {
        val reservation = Reservation(null, reservationDTO.adId, reservationDTO.userId, reservationDTO.beginDate, reservationDTO.endDate, reservationDTO.statusId)

        if (reservationDTO.endDate.isBefore(reservationDTO.beginDate) || reservationDTO.beginDate.isBefore(LocalDateTime.now()) || reservationDTO.endDate.isBefore(LocalDateTime.now()) || reservationDTO.statusId < 1 || reservationDTO.statusId > 3 || getAdbyIdUseCase.execute(reservationDTO.adId) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }

        val createdReservation = createUseCase.execute(reservation)
        return Response.status(Response.Status.CREATED).entity(ReservationDTO.fromReservation(createdReservation)).build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ad/{adId}")
    fun getListByAd(@PathParam("adId") adId: Int): List<ReservationDTO> {
        val reservations = getListByAdUseCase.execute(adId)
        return reservations.map { ReservationDTO.fromReservation(it) }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status/{statusId}")
    fun getListByStatus(@PathParam("statusId") statusId: Int): List<ReservationDTO> {
        val reservations = getListByStatusUseCase.execute(statusId)
        return reservations.map { ReservationDTO.fromReservation(it) }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    fun update(@PathParam("id") reservationId: Int, reservationDTO: ReservationDTO): Response {
        val reservation = Reservation(null, reservationDTO.adId, reservationDTO.userId, reservationDTO.beginDate, reservationDTO.endDate, reservationDTO.statusId)

        if (reservationDTO.endDate.isBefore(reservationDTO.beginDate) || reservationDTO.beginDate.isBefore(LocalDateTime.now()) || reservationDTO.endDate.isBefore(LocalDateTime.now()) || reservationDTO.statusId < 1 || reservationDTO.statusId > 3) {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
        updateUseCase.execute(reservationId, reservation)
        return Response.ok(ReservationDTO.fromReservation(reservation)).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/cancel/{id}")
    fun cancel(@PathParam("id") reservationId: Int): Response {
        val reservation = cancelUseCase.execute(reservationId)

        return if (reservation != null) {
            return Response.ok().build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun delete(@PathParam("id") reservationId: Int): Response {
        deleteUseCase.execute(reservationId)
        return Response.noContent().build()
    }

}