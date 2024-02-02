package org.rncp.user.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.user.domain.model.User

@Entity
@Table(name="\"user\"")
data class UserDAO(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val uid: String,
        val firstname: String,
        val lastname: String,
        val email: String,
        val roleId: Int,
): PanacheEntityBase() {
    constructor(): this(null, "", "", "", "", 0)

    fun toUser(): User {
        return User(id, uid, firstname, lastname, email, roleId)
    }
}
