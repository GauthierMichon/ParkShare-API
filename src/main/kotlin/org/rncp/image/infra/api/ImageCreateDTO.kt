package org.rncp.image.infra.api

import kotlinx.serialization.Serializable

@Serializable
class ImageCreateDTO(
        var adId: Int,
        var imageData: ByteArray
) {
    companion object {
        fun fromImage(image: ImageDTO): ImageCreateDTO {
            return ImageCreateDTO(
                    adId = image.adId,
                    imageData = image.imageData,
            )
        }
    }
}