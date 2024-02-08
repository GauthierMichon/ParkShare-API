package org.rncp.status.domain.model

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.rncp.status.domain.ports.`in`.GetStatusByIdUseCase
import org.rncp.status.infra.api.StatusDto

@Path("/api/status")
class StatusResource {

    @Inject
    lateinit var getOneUseCase: GetStatusByIdUseCase

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") statusId: Int): Response {
        val status = getOneUseCase.execute(statusId)

        return if (status != null) {
            Response.ok(StatusDto.fromStatus(status)).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

}