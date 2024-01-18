package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable

@Entity
@Serializable
@Table(name = "app_user")
data class User(
        @Id
        var user_id: Int,
        var firstname: String?,
        var lastname: String?,
        var email_address: String?,
        var password: String?,
        var role_id: Int?,
) : PanacheEntityBase() {
    constructor() : this(0, null, null, "", "", 0)
}

