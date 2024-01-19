package org.rncp.ad.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Ad(
        var ad_id: Int,
        var user_id: Int,
        var name: String,
        var description: String,
        var hour_price: Float,
        var latitude: String,
        var longitude: String
) {
    constructor() : this(0, 0, "", "", 0.0f, "", "")
}
