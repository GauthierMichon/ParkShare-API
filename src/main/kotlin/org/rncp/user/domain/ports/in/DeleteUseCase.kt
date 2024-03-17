package org.rncp.user.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.firebase.FirebaseService
import org.rncp.user.domain.ports.out.UserRepository

@ApplicationScoped
class DeleteUseCase {
    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var firebaseService: FirebaseService

    fun execute(userUid: String) {
        firebaseService.deleteUser(userUid)
        userRepository.delete(userUid)
    }
}