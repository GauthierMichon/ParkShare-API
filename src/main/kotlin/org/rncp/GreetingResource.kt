package org.rncp

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/hello")
class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "Hello from RESTEasy Reactive"
}

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