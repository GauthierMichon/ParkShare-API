package org.rncp.image.domain.ports.out

import org.rncp.image.domain.model.Image

interface ImageRepository {
    fun create(image: Image): Image
    fun getById(id: Int): Image?
    fun getListByAd(adId: Int): List<Image>
    fun delete(imageId: Int)
}