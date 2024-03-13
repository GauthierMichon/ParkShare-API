package org.rncp.user.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.user.domain.model.User

@Entity
@Table(name="app_user")
data class UserDAO(
        @Id
        var uid: String,
        var firstname: String,
        var lastname: String,
        var email: String,
        var roleId: Int,
): PanacheEntityBase() {
    constructor(): this("", "", "", "", 0)

    fun toUser(): User {
        return User(uid, firstname, lastname, email, roleId)
    }
}
