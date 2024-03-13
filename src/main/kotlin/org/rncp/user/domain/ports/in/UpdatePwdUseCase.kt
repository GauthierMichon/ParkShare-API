package org.rncp.user.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.rncp.firebase.FirebaseService

@ApplicationScoped
class UpdatePwdUseCase {

    @Inject
    private lateinit var firebaseService: FirebaseService

    fun execute(uid: String, pwd: String): Response {
        firebaseService.updatePwd(uid, pwd)
        return Response.noContent().build()
    }
}