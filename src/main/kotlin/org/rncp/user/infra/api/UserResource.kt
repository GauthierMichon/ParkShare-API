package org.rncp.user.infra.api

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.`in`.DeleteUseCase
import org.rncp.user.domain.ports.`in`.LoginUseCase
import org.rncp.user.domain.ports.`in`.RegisterUseCase
import org.rncp.user.domain.ports.`in`.UpdateUseCase


@Path("/api/user")
class UserResource {

    @Inject
    lateinit var registerUseCase: RegisterUseCase

    @Inject
    lateinit var loginUseCase: LoginUseCase

    @Inject
    lateinit var updateUseCase: UpdateUseCase

    @Inject
    lateinit var deleteUseCase: DeleteUseCase

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
    fun authenticate(loginDTO: LoginDTO): Response {
        val url = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyDDH1H4hx2AOHd0NECecYA-MfDUxhDDECM"

        val jsonBody = """
            {
                "email": "${loginDTO.email}",
                "password": "${loginDTO.password}",
                "returnSecureToken": ${loginDTO.returnSecureToken}
            }
        """.trimIndent()

        val response = loginUseCase.execute(url, jsonBody)

        val responseBody = response.body()?.string() ?: ""

        return Response.status(response.code())
                .entity(responseBody)
                .build()
    }


    @PUT
    @Path("/{uid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    fun update(@PathParam("uid") userUid: String, userUpdateDTO: UserUpdateDTO): Response {
        val user = User(userUid, userUpdateDTO.firstname, userUpdateDTO.lastname, userUpdateDTO.email, userUpdateDTO.roleId)
        return updateUseCase.execute(user)
    }

    @DELETE
    @Path("/{uid}")
    @Transactional
    @Authenticated
    fun delete(@PathParam("uid") userUid: String): Response {
        deleteUseCase.execute(userUid)
        return Response.noContent().build()
    }
}