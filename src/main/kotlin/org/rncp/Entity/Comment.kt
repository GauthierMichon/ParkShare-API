package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Comment(
        @Id
        var comment_id: Int,
        var annoncement_id: Int,
        var user_id: Int,
        var description: String,
        var date: String,
) : PanacheEntityBase() {
    constructor() : this(0, 0, 0, "", "")
}
