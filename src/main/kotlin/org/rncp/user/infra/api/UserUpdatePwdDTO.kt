package org.rncp.user.infra.api

import kotlinx.serialization.Serializable

@Serializable
class UserUpdatePwdDTO(
    val password: String
) {
}