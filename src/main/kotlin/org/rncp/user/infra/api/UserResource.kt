package org.rncp.user.infra.api

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import okhttp3.OkHttpClient
import okhttp3.MediaType as okhttp3MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.`in`.*


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

    @Inject
    lateinit var updatePwdUseCase: UpdatePwdUseCase

    @Inject
    lateinit var refreshJwtUseCase: RefreshJwtUseCase

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


    @PATCH
    @Path("/changepwd/{uid}")
    @Authenticated
    fun changePwd(@PathParam("uid") userUid: String, userUpdatePwdDTO: UserUpdatePwdDTO):Response {
        return updatePwdUseCase.execute(userUid, userUpdatePwdDTO.password)
    }


    @POST
    @Path("refreshjwt/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    fun refreshJwt(@PathParam("uid") userUid: String): Response {
        val newToken = refreshJwtUseCase.execute(userUid)
        return Response.ok().entity("""
            {
                "jwttoken": "$newToken"
            }
        """.trimIndent()).build()
    }
}