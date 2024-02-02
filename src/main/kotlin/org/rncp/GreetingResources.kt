package org.rncp

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
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

/*
@Path("/api/users")
class UserResource {

    @Inject
    lateinit var userRepository: UserRepository
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllUsers(): List<UserDto> {
        val users = userRepository.listAll()
        return users.map { user ->
            val link = "/api/users/${user.user_id}"
            UserDto(user, link)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getUserById(@PathParam("id") userId: Int): User? {
        return userRepository.findById(userId)
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(user: User): Response {
        user.persist()
        return Response.status(Response.Status.CREATED).entity(user).build()
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateUser(@PathParam("id") userId: Int, updatedUser: User): Response {
        val existingUser = userRepository.findById(userId)
        existingUser?.apply {
            firstname = updatedUser.firstname
            lastname = updatedUser.lastname
            email_address = updatedUser.email_address
            password = updatedUser.password
            role_id = updatedUser.role_id
        }

        if (existingUser != null) {
            userRepository.persist(existingUser) // Persistez les modifications dans la base de données
            return Response.ok(existingUser).build()
        } else {
            return Response.status(404).build()
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun patchUser(@PathParam("id") userId: Int, patchedUser: User): Response {
        val existingUser = userRepository.findById(userId)

        if (existingUser != null) {
            existingUser.firstname = patchedUser.firstname ?: existingUser.firstname
            existingUser.lastname = patchedUser.lastname ?: existingUser.lastname
            existingUser.email_address = patchedUser.email_address ?: existingUser.email_address
            existingUser.password = patchedUser.password ?: existingUser.password
            existingUser.role_id = patchedUser.role_id ?: existingUser.role_id

            userRepository.persist(existingUser)
            return Response.ok(existingUser).build()
        } else {
            return Response.status(404).build()
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun deleteUser(@PathParam("id") userId: Int): Response {
        userRepository.deleteById(userId)
        return Response.noContent().build()
    }
}*/
