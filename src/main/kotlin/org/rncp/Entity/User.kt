package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
data class User(
        var user_id: Int,
        var firstname: String?,
        var lastname: String?,
        var email_address: String,
        var password: String,
        var role_id: Int,
) : PanacheEntity() {
    constructor() : this(0, null, null, "", "", 0)
}

