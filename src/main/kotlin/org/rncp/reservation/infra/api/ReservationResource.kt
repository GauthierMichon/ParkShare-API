package org.rncp.reservation.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.domain.ports.`in`.*


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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(
            summary = "Get a friendly greeting",
            description = "Returns a plain text greeting"
    )
    fun create(reservationDTO: ReservationDTO): Response {
        val reservation = Reservation(null, reservationDTO.adId, reservationDTO.userId, reservationDTO.beginDate, reservationDTO.endDate, reservationDTO.statusId)
        createUseCase.execute(reservation)
        return Response.status(Response.Status.CREATED).entity(ReservationDTO.fromReservation(reservation)).build()
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
    @Path("/ad/{statusId}")
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
        updateUseCase.execute(reservationId, reservation)
        return Response.ok(ReservationDTO.fromReservation(reservation)).build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun delete(@PathParam("id") reservationId: Int): Response {
        deleteUseCase.execute(reservationId)
        return Response.noContent().build()
    }

}