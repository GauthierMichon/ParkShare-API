package org.rncp.user.infra.api

import kotlinx.serialization.Serializable
import org.rncp.user.domain.model.User

@Serializable
class UserUpdateDTO(
    val firstname: String,
    val lastname: String,
    val email: String,
    val roleId: Int,
) {
    companion object {
        fun fromUser(user: User): UserUpdateDTO {
            return UserUpdateDTO(user.firstname, user.lastname, user.email, user.roleId)
        }
    }
}