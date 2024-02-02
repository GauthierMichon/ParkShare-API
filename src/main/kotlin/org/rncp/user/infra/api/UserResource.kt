package org.rncp.user.infra.api

import io.quarkus.security.Authenticated
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.`in`.RegisterUseCase

@Path("/api/user")
class UserResource {

    @Inject
    lateinit var registerUseCase: RegisterUseCase

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun register(userDTO: UserDTO): Response {
        val user = User(null, userDTO.firstname, userDTO.lastname, userDTO.email, userDTO.roleId)
        registerUseCase.execute(user, userDTO.password!!)
        return Response.ok(UserDTO.fromUser(user)).build()
    }

    @POST
    @Path("/authentication")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    fun authenticate(@Context securityContext: SecurityContext, @HeaderParam("Authorization") authorizationHeader: String): Response {
        println(authorizationHeader.removePrefix("Bearer "))
        return Response.ok().build()
    }
}