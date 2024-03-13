package org.rncp.user.domain.ports.out

import org.rncp.user.domain.model.User

interface UserRepository {
    fun getByUid(uid: String): User?

    fun create(user: User): User

    fun update(userData: User)

    fun delete(userUid: String)
}