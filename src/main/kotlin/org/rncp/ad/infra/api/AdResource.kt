package org.rncp.ad.infra.api

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.`in`.*
import org.rncp.reservation.domain.model.Reservation
import org.rncp.reservation.infra.api.ReservationDTO

@Path("/api/ads")
class AdResource {

    @Inject
    lateinit var deleteUseCase: DeleteUseCase

    @Inject
    lateinit var createUseCase: CreateUseCase

    @Inject
    lateinit var getAllUseCase: GetAllUseCase

    @Inject
    lateinit var getOneUseCase: GetByIdUseCase

    @Inject
    lateinit var updateUseCase: UpdateUseCase

    @Inject
    lateinit var publishUseCase: PublishUseCase

    @Inject
    lateinit var unpublishUseCase: UnpublishUseCase

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): List<AdDto> {
        val ads = getAllUseCase.execute()
        return ads.map { ad ->
            val link = "/api/ads/${ad.id}"
            AdDto.fromAd(ad, link)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") adId: Int): Response {
        val ad = getOneUseCase.execute(adId)
        return Response.ok(AdDto.fromAd(ad, "")).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun create(adDto: AdDto): Response {
        val ad = Ad(null, adDto.userId, adDto.name, adDto.description, adDto.hourPrice, adDto.latitude, adDto.longitude, adDto.state)
        val createdAd = createUseCase.execute(ad)
        return Response.status(Response.Status.CREATED).entity(AdDto.fromAd(createdAd, "")).build()
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun update(@PathParam("id") adId: Int, adDto: AdDto): Response {
        val adData = Ad(null, adDto.userId, adDto.name, adDto.description, adDto.hourPrice, adDto.latitude, adDto.longitude, adDto.state)
        val updatedAd = updateUseCase.execute(adId, adData)
        return Response.ok(AdDto.fromAd(updatedAd, "")).build()
    }

    @POST
    @Path("/{id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun publish(@PathParam("id") adId: Int): Response {
        publishUseCase.execute(adId)
        return Response.ok().build()
    }

    @POST
    @Path("/{id}/unpublish")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun unpublish(@PathParam("id") adId: Int): Response {
        unpublishUseCase.execute(adId)
        return Response.ok().build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun delete(@PathParam("id") adId: Int): Response {
        deleteUseCase.execute(adId)
        return Response.noContent().build()
    }
}