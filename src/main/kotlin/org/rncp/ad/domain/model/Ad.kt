package org.rncp.ad.domain.model

data class Ad(
        var id: Int? = null,
        var userId: String,
        var name: String,
        var description: String,
        var hourPrice: Double,
        var latitude: Double,
        var longitude: Double,
        var state: Boolean,
) {
    constructor() : this(null,"", "", "", 0.0, 0.0, 0.0, false)
}
