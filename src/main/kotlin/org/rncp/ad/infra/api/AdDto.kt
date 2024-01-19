package org.rncp.ad.infra.api

import org.rncp.ad.domain.model.Ad
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.serialization.Serializable
@Serializable
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AdDto(
        //val ad: Ad,
        val ad_id: Int,
        val user_id: Int?,
        val name: String?,
        val description: String?,
        val hour_price: Float?,
        val latitude: String?,
        val longitude: String?,
        val state: Boolean,
        val link: String
)