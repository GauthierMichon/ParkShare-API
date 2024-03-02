package org.rncp.user.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.firebase.FirebaseService
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.out.UserRepository

@ApplicationScoped
class RegisterUseCase {

    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var firebaseService: FirebaseService

    fun execute(userData: User, password: String): User {
        val uid = firebaseService.registerUserInFirebase(userData, password)
        userData.uid = uid
        return userRepository.create(userData)
    }
}