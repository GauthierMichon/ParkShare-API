package org.rncp.user.infra.api

import kotlinx.serialization.Serializable
import org.rncp.user.domain.model.User

@Serializable
data class UserDTO(
        val id: Int? = null,
        val uid: String? = null,
        val password: String? = null,
        val firstname: String,
        val lastname: String,
        val email: String,
        val roleId: Int,
) {
    companion object {
        fun fromUser(user: User): UserDTO {
            return UserDTO(
                    id = user.id,
                    uid = user.uid,
                    null,
                    firstname = user.firstname,
                    lastname = user.lastname,
                    email = user.email,
                    roleId = user.roleId
            )
        }
    }
}
