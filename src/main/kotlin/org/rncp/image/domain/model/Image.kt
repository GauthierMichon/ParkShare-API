package org.rncp.image.domain.model

data class Image(
        var id: Int? = null,
        var adId: Int,
        var imageData: ByteArray
) {
    constructor() : this(0, 0, byteArrayOf())
}
