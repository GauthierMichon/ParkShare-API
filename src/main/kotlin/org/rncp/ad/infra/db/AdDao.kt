package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import org.rncp.ad.domain.model.Ad

@Entity
@Table(name = "ad")
data class AdDao(
        @Id @GeneratedValue
        var id: Int? = null,
        var userId: String,
        var name: String,
        var description: String,
        var hourPrice: Float,
        var latitude: String,
        var longitude: String,
        var state: Boolean
) : PanacheEntityBase() {
    constructor() : this(0, "", "", "", 0.0f, "", "", false)

    fun toAd(): Ad {
        return Ad(null, userId, name, description, hourPrice, latitude, longitude, state)
    }
}

// Builder fromAd companion object Ad -> AdDao
