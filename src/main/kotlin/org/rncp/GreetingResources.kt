package org.rncp

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/api")
class PingResource {

    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    fun ping(): Response {
        val status = "OK"
        val pingResponse = PingResponse(status)

        return Response
                .status(if (status == "OK") 200 else 500)
                .entity(pingResponse)
                .build()
    }
}