package org.rncp.ad.domain.model

data class Ad(
        var id: Int? = null,
        var userId: String,
        var name: String,
        var description: String,
        var hourPrice: Float,
        var latitude: String,
        var longitude: String,
        var state: Boolean,
) {
    constructor() : this(null,"", "", "", 0.0f, "", "", false)
}
