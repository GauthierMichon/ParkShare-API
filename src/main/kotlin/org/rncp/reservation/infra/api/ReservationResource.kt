package org.rncp.reservation.infra.api

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.rncp.ad.domain.ports.`in`.GetAdByIdUseCase
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
    lateinit var acceptUseCase: AcceptUseCase

    @Inject
    lateinit var cancelUseCase: CancelUseCase

    @Inject
    lateinit var getOneUseCase: GetByIdUseCase

    @Inject
    lateinit var getAdbyIdUseCase: GetAdByIdUseCase


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
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
    @Authenticated
    fun create(reservationCreateOrUpdateDTO: ReservationCreateOrUpdateDTO, @Context securityContext: SecurityContext): Response {
        val userUid = securityContext.userPrincipal.name
        val reservation = Reservation(null, reservationCreateOrUpdateDTO.adId, userUid, reservationCreateOrUpdateDTO.beginDate, reservationCreateOrUpdateDTO.endDate, null, reservationCreateOrUpdateDTO.statusId)

        if (reservationCreateOrUpdateDTO.endDate.isBefore(reservationCreateOrUpdateDTO.beginDate) || reservationCreateOrUpdateDTO.beginDate.isBefore(LocalDateTime.now()) || reservationCreateOrUpdateDTO.endDate.isBefore(LocalDateTime.now()) || reservationCreateOrUpdateDTO.statusId < 1 || reservationCreateOrUpdateDTO.statusId > 3 || getAdbyIdUseCase.execute(reservationCreateOrUpdateDTO.adId) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }

        val createdReservation = createUseCase.execute(reservation)
        return Response.status(Response.Status.CREATED).entity(ReservationDTO.fromReservation(createdReservation)).build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ad/{adId}")
    @Authenticated
    fun getListByAd(@PathParam("adId") adId: Int): List<ReservationDTO> {
        val reservations = getListByAdUseCase.execute(adId)
        return reservations.map { ReservationDTO.fromReservation(it) }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status/{statusId}")
    @Authenticated
    fun getListByStatus(@PathParam("statusId") statusId: Int): List<ReservationDTO> {
        val reservations = getListByStatusUseCase.execute(statusId)
        return reservations.map { ReservationDTO.fromReservation(it) }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    @Authenticated
    fun update(@PathParam("id") reservationId: Int, reservationCreateOrUpdateDTO: ReservationCreateOrUpdateDTO, @Context securityContext: SecurityContext): Response {
        val userUid = securityContext.userPrincipal.name
        val reservation = Reservation(reservationId, reservationCreateOrUpdateDTO.adId, userUid, reservationCreateOrUpdateDTO.beginDate, reservationCreateOrUpdateDTO.endDate, 0.0, reservationCreateOrUpdateDTO.statusId)

        if (reservationCreateOrUpdateDTO.endDate.isBefore(reservationCreateOrUpdateDTO.beginDate) || reservationCreateOrUpdateDTO.beginDate.isBefore(LocalDateTime.now()) || reservationCreateOrUpdateDTO.endDate.isBefore(LocalDateTime.now()) || reservationCreateOrUpdateDTO.statusId < 1 || reservationCreateOrUpdateDTO.statusId > 3) {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
        return updateUseCase.execute(reservationId, reservation)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/accept/{id}")
    @Authenticated
    fun accept(@PathParam("id") reservationId: Int): Response {
        return acceptUseCase.execute(reservationId)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/cancel/{id}")
    @Authenticated
    fun cancel(@PathParam("id") reservationId: Int): Response {
        return cancelUseCase.execute(reservationId)
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Authenticated
    fun delete(@PathParam("id") reservationId: Int): Response {
        deleteUseCase.execute(reservationId)
        return Response.noContent().build()
    }

}