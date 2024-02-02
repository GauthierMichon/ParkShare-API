package org.rncp.user.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.firebase.FirebaseService

@ApplicationScoped
class LoginUseCase {
    @Inject
    private lateinit var firebaseService: FirebaseService

    fun execute() {
        firebaseService.authenticateWithFirebase()
    }
}