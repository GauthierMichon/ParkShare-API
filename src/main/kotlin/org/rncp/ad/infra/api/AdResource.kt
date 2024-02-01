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
            val link = "/api/ads/${ad.userId}"
            AdDto.fromAd(ad, link)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAdById(@PathParam("id") adId: Int): AdDto? {
        val ad = getOneUseCase.execute(adId)
        return AdDto.fromAd(ad, "")
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun create(adDto: AdDto): Response {
        val ad = Ad(null, adDto.userId, adDto.name, adDto.description, adDto.hourPrice, adDto.latitude, adDto.longitude, adDto.state)
        createUseCase.execute(ad)
        return Response.status(Response.Status.CREATED).entity(AdDto.fromAd(ad, "")).build()
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun update(@PathParam("id") adId: Int, updatedAdDto: AdDto): Response {
        val updatedAd = Ad(null, updatedAdDto.userId, updatedAdDto.name, updatedAdDto.description, updatedAdDto.hourPrice, updatedAdDto.latitude, updatedAdDto.longitude, updatedAdDto.state)
        updateUseCase.execute(adId, updatedAd)
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