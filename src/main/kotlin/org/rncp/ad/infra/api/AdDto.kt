package org.rncp.ad.infra.api

import kotlinx.serialization.Serializable
import org.rncp.ad.domain.model.Ad

@Serializable
data class AdDto(
        val id: Int? = null,
        val userId: String,
        val name: String,
        val description: String,
        val hourPrice: Float,
        val latitude: String,
        val longitude: String,
        val state: Boolean,
        val link: String
) {
    companion object {
        fun fromAd(ad: Ad): AdDto {
            return AdDto(
                    id = ad.id,
                    userId = ad.userId,
                    name = ad.name,
                    description = ad.description,
                    hourPrice = ad.hourPrice,
                    latitude = ad.latitude,
                    longitude = ad.longitude,
                    state = ad.state,
                    link = "",
            )
        }
    }
}