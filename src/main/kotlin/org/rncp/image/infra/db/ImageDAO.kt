package org.rncp.image.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.infra.db.AdDao
import org.rncp.image.domain.model.Image

@Entity
@Table(name = "image")
data class ImageDAO(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @ManyToOne(targetEntity = AdDao::class, fetch = FetchType.LAZY)
        @JoinColumn(name="ad")
        var ad: AdDao,
        @Column(name = "image_data", nullable = false)
        var imageData: ByteArray,
) : PanacheEntityBase() {
    constructor() : this(0, AdDao(), byteArrayOf())

    fun toImage(): Image {
        return Image(id, ad.id!!, imageData)
    }
}