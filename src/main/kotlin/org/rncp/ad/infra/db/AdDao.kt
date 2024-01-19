package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import kotlinx.serialization.Serializable
import org.rncp.ad.domain.model.Ad

@Entity
@Serializable
data class AdDao(
        @Id @GeneratedValue
        var ad_id: Int,
        var user_id: Int?,
        var name: String?,
        var description: String?,
        var hour_price: Float?,
        var latitude: String?,
        var longitude: String?,
        var state: Boolean?
) : PanacheEntityBase() {
    constructor() : this(0, 0, "", "", 0.0f, "", "", false)

    fun toAd(): Ad {
        return Ad(ad_id, user_id, name, description, hour_price, latitude, longitude, state)
    }
}

// Builder fromAd companion object Ad -> AdDao