package org.rncp.ad.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.domain.ports.`in`.CreateUseCase
import org.rncp.ad.domain.ports.`in`.DeleteUseCase
import org.rncp.ad.infra.db.AdPostGreRepository

@Path("/api/ads")
class AdResource {

    @Inject
    lateinit var adRepository: AdPostGreRepository // TODO : remove
    
    @Inject
    lateinit var deleteUseCase: DeleteUseCase
    @Inject
    lateinit var createUseCase: CreateUseCase

    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllAds(): List<AdDto> {
        val ads = adRepository.listAll()
        return ads.map { ad ->
            val link = "/api/ads/${ad.ad_id}"
            AdDto(ad, link)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAdById(@PathParam("id") adId: Int): Ad? {
        return adRepository.findById(adId)
    }*/

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createAd(ad: Ad): Response {
        createUseCase.execute(ad)
        return Response.status(Response.Status.CREATED).entity(ad).build()
    }

    /*@PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun patchAd(@PathParam("id") adId: Int): Ad? {
        // TODO
        return adRepository.findById(adId)
    }

    @POST
    @Path("/{id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    fun postPublish(@PathParam("id") adId: Int): Ad? {
        // TODO
        return adRepository.findById(adId)
    }

    @POST
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