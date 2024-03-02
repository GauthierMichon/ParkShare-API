package org.rncp.image.infra.api

import kotlinx.serialization.Serializable
import org.rncp.image.domain.model.Image

@Serializable
data class ImageDTO(
        var id: Int? = null,
        var adId: Int,
        var imageData: ByteArray
) {
    companion object {
        fun fromImage(image: Image): ImageDTO {
            return ImageDTO(
                    id = image.id,
                    adId = image.adId,
                    imageData = image.imageData,
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageDTO

        if (id != other.id) return false
        if (adId != other.adId) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + adId
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}