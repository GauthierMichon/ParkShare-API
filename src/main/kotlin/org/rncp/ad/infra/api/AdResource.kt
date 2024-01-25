package org.rncp.ad.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.`in`.*

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
    fun getAllAds(): List<AdDto> {
        val ads = getAllUseCase.execute()
        return ads.map { ad ->
            val link = "/api/ads/${ad.userId}"
            AdDto(null, ad.userId, ad.name, ad.description, ad.hourPrice, ad.latitude, ad.longitude, ad.state!!, link)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAdById(@PathParam("id") adId: Int): Ad? {
        // TODO : Gérer si l'id n'existe pas
        return getOneUseCase.execute(adId)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun createAd(adDto: AdDto): Response {
        val ad = Ad(null, adDto.userId, adDto.name, adDto.description, adDto.hourPrice, adDto.latitude, adDto.longitude, adDto.state)
        createUseCase.execute(ad)
        return Response.status(Response.Status.CREATED).entity(AdDto.fromAd(ad)).build()
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateAd(@PathParam("id") adId: Int, updatedAdDto: AdDto): Response {
        val updatedAd = Ad(null, updatedAdDto.userId, updatedAdDto.name, updatedAdDto.description, updatedAdDto.hourPrice, updatedAdDto.latitude, updatedAdDto.longitude, updatedAdDto.state)
        updateUseCase.execute(adId, updatedAd)
        return Response.ok(AdDto.fromAd(updatedAd)).build()
    }

    @POST
    @Path("/{id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun postPublish(@PathParam("id") adId: Int): Response {
        publishUseCase.execute(adId)
        return Response.ok().build()
    }

    @POST
    @Path("/{id}/unpublish")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun postUnpublish(@PathParam("id") adId: Int): Response {
        unpublishUseCase.execute(adId)
        return Response.ok().build()
    }

    @DELETE
    @Path("/{id}")
    fun deleteAd(@PathParam("id") adId: Int): Response {
        deleteUseCase.deleteAd(adId)
        return Response.noContent().build()
    }
}