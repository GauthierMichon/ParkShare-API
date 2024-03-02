package org.rncp.image.infra.api

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.rncp.ad.domain.ports.`in`.GetAdByIdUseCase
import org.rncp.image.domain.model.Image
import org.rncp.image.domain.ports.`in`.*


@Path("/api/image")
class ImageResource {
    @Inject
    lateinit var createUseCase: CreateUseCase

    @Inject
    lateinit var getListByAdUseCase: GetListByAdUseCase

    @Inject
    lateinit var deleteUseCase: DeleteUseCase

    @Inject
    lateinit var getOneUseCase: GetByIdUseCase

    @GET
    @Path("/{id}")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") adId: Int): Response {
        val image = getOneUseCase.execute(adId)

        return if (image != null) {
            return Response.ok(ImageDTO.fromImage(image)).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    fun create(imageCreateDTO: ImageCreateDTO, @Context securityContext: SecurityContext): Response {
        val image = Image(null, imageCreateDTO.adId, imageCreateDTO.imageData)
        val createdImage = createUseCase.execute(image)
        return Response.status(Response.Status.CREATED).entity(ImageDTO.fromImage(createdImage)).build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ad/{adId}")
    @Authenticated
    fun getListByAd(@PathParam("adId") adId: Int): List<ImageDTO> {
        val images = getListByAdUseCase.execute(adId)
        return images.map { ImageDTO.fromImage(it) }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Authenticated
    fun delete(@PathParam("id") imageId: Int): Response {
        deleteUseCase.execute(imageId)
        return Response.noContent().build()
    }

}