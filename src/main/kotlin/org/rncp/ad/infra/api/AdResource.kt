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
    lateinit var patchUseCase: PatchUseCase

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllAds(): List<AdDto> {
        return getAllUseCase.execute()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAdById(@PathParam("id") adId: Int): Ad? {
        // TODO : Gérer si l'id n'existe pas
        return getOneUseCase.execute(adId)
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createAd(ad: Ad): Response {
        createUseCase.execute(ad)
        return Response.status(Response.Status.CREATED).entity(ad).build()
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun patchAd(@PathParam("id") adId: Int, updatedAd: Ad): Response {
        return patchUseCase.execute(adId, updatedAd)
    }

    /*@POST
    @Path("/{id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    fun postPublish(@PathParam("id") adId: Int): Ad? {
        // TODO
        return adRepository.findById(adId)
    }*/

    /*@POST
    @Path("/{id}/unpublish")
    @Produces(MediaType.APPLICATION_JSON)
    fun postUnpublish(@PathParam("id") adId: Int): Ad? {
        // TODO
        return adRepository.findById(adId)
    }*/

    @DELETE
    @Transactional
    @Path("/{id}")
    fun deleteAd(@PathParam("id") adId: Int): Response {
        // TODO
        deleteUseCase.deleteAd(adId)
        // adRepository.deleteById(adId)
        return Response.noContent().build()
    }
}