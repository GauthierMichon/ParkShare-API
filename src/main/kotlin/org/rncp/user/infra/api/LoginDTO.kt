package org.rncp.user.infra.api

import kotlinx.serialization.Serializable

@Serializable
class LoginDTO(
        val email: String,
        val password: String,
        val returnSecureToken: Boolean
) {
}