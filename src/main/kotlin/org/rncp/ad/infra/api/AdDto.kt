package org.rncp.ad.infra.api

import org.rncp.ad.domain.model.Ad
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.serialization.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AdDto(
        val ad: Ad,
        val link: String
)