package org.rncp.user.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import okhttp3.OkHttpClient
import okhttp3.MediaType as okhttp3MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.`in`.LoginUseCase
import org.rncp.user.domain.ports.`in`.RegisterUseCase


@Path("/api/user")
class UserResource {

    @Inject
    lateinit var registerUseCase: RegisterUseCase

    @Inject
    lateinit var loginUseCase: LoginUseCase

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
}