package org.rncp.user.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.out.UserRepository

@ApplicationScoped
class GetByUidUseCase {
    @Inject
    private lateinit var userRepository: UserRepository

    fun execute(uid: String): User {
        return userRepository.getByUid(uid)
    }
}