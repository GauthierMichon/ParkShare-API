package org.rncp.ad.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Entity
import jakarta.persistence.Id
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class AdDao(
        @Id
        var ad_id: Int,
        var user_id: Int,
        var name: String,
        var description: String,
        var hour_price: Float,
        var latitude: String,
        var longitude: String
) : PanacheEntityBase() {
    constructor() : this(0, 0, "", "", 0.0f, "", "")
}