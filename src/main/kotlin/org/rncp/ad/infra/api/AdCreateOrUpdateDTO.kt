package org.rncp.ad.infra.api

import kotlinx.serialization.Serializable
import org.rncp.ad.domain.model.Ad


@Serializable
class AdCreateOrUpdateDTO(
        val name: String,
        val description: String,
        val hourPrice: Float,
        val latitude: Float,
        val longitude: Float,
        val state: Boolean,
        val link: String
) {
    companion object {
        fun fromAd(ad: Ad, link: String): AdCreateOrUpdateDTO {
            return AdCreateOrUpdateDTO(
                    name = ad.name,
                    description = ad.description,
                    hourPrice = ad.hourPrice,
                    latitude = ad.latitude,
                    longitude = ad.longitude,
                    state = ad.state,
                    link = link
            )
        }
    }
}