package org.rncp.Dto

import org.rncp.Entity.User
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.serialization.Serializable

@Serializable
@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto(
        val user: User,
        val link: String
)
