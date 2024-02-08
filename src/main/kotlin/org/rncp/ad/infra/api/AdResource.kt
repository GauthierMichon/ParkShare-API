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

@Path("/api/ads")
class AdResource {

    @Inject
    lateinit var deleteUseCase: DeleteUseCase

    @Inject
    lateinit var createUseCase: CreateUseCase

    @Inject
    lateinit var getAllUseCase: GetAllUseCase

    @Inject
    lateinit var getOneUseCase: GetAdByIdUseCase

    @Inject
    lateinit var updateUseCase: UpdateUseCase

    @Inject
    lateinit var publishUseCase: PublishUseCase

    @Inject
    lateinit var unpublishUseCase: UnpublishUseCase

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
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
    @Authenticated
    fun getById(@PathParam("id") adId: Int): Response {
        val ad = getOneUseCase.execute(adId)

        return if (ad != null) {
            Response.ok(AdDto.fromAd(ad, "")).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    fun create(adCreateOrUpdateDTO: AdCreateOrUpdateDTO, @Context securityContext: SecurityContext): Response {
        val userUid = securityContext.userPrincipal.name
        val ad = Ad(null, userUid, adCreateOrUpdateDTO.name, adCreateOrUpdateDTO.description, adCreateOrUpdateDTO.hourPrice, adCreateOrUpdateDTO.latitude, adCreateOrUpdateDTO.longitude, adCreateOrUpdateDTO.state)
        if (adCreateOrUpdateDTO.hourPrice < 0f || adCreateOrUpdateDTO.latitude < -90 || adCreateOrUpdateDTO.latitude > 90 || adCreateOrUpdateDTO.longitude < -180 || adCreateOrUpdateDTO.longitude > 180) {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }

        val createdAd = createUseCase.execute(ad)
        return Response.status(Response.Status.CREATED).entity(AdDto.fromAd(createdAd, "")).build()
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    fun update(@PathParam("id") adId: Int, adCreateOrUpdateDTO: AdCreateOrUpdateDTO, @Context securityContext: SecurityContext): Response {
        val userUid = securityContext.userPrincipal.name
        val adData = Ad(adId, userUid, adCreateOrUpdateDTO.name, adCreateOrUpdateDTO.description, adCreateOrUpdateDTO.hourPrice, adCreateOrUpdateDTO.latitude, adCreateOrUpdateDTO.longitude, adCreateOrUpdateDTO.state)
        if (adCreateOrUpdateDTO.hourPrice < 0f || adCreateOrUpdateDTO.latitude < -90 || adCreateOrUpdateDTO.latitude > 90 || adCreateOrUpdateDTO.longitude < -180 || adCreateOrUpdateDTO.longitude > 180) {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
        val updatedAd = updateUseCase.execute(adData)
        return Response.ok(AdDto.fromAd(updatedAd, "")).build()
    }

    @POST
    @Path("/{id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    fun publish(@PathParam("id") adId: Int): Response {
        publishUseCase.execute(adId)
        return Response.ok().build()
    }

    @POST
    @Path("/{id}/unpublish")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    fun unpublish(@PathParam("id") adId: Int): Response {
        unpublishUseCase.execute(adId)
        return Response.ok().build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Authenticated
    fun delete(@PathParam("id") adId: Int): Response {
        val isDeleted = deleteUseCase.execute(adId)
        return if (isDeleted) {
            Response.noContent().build()
        } else {
            Response.status(Response.Status.CONFLICT)
                    .entity("La suppression de l'annonce n'est pas autorisée car des réservations actives sont liées à cette annonce.")
                    .build()
        }
    }
}