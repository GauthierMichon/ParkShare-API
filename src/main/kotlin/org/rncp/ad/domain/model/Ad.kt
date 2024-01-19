package org.rncp.ad.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Ad(
        var user_id: String,
        var name: String,
        var description: String,
        var hour_price: Float,
        var latitude: String,
        var longitude: String
) {
    constructor() : this("", "", "", 0.0f, "", "")
}
