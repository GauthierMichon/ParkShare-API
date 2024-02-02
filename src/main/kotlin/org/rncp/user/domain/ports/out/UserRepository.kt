package org.rncp.user.domain.ports.out

import org.rncp.user.domain.model.User

interface UserRepository {
    fun getByIdUid(uid: String): User?

    fun create(user: User): User
}