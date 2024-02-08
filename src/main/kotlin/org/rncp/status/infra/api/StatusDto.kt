package org.rncp.status.infra.api

import kotlinx.serialization.Serializable
import org.rncp.status.domain.model.Status

@Serializable
data class StatusDto(
        val id: Int? = null,
        val label: String,
) {
    companion object {
        fun fromStatus(status: Status): StatusDto {
            return StatusDto(
                    id = status.id,
                    label = status.label,
            )
        }
    }
}