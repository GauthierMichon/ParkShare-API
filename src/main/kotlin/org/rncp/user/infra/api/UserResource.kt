package org.rncp.user.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
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
        val user = User(null, null, userDTO.firstname, userDTO.lastname, userDTO.email, userDTO.roleId)
        registerUseCase.execute(user, userDTO.password!!)
        return Response.ok(UserDTO.fromUser(user)).build()
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun login(userDTO: UserDTO): Response {

        return Response.ok().build()
    }
}