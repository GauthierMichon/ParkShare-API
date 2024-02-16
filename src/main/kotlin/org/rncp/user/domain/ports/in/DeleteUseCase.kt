package org.rncp.user.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.user.domain.ports.out.UserRepository

@ApplicationScoped
class DeleteUseCase {
    @Inject
    private lateinit var userRepository: UserRepository

    fun execute(userUid: String) {
        userRepository.delete(userUid)
    }
}