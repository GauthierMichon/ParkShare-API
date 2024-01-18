package org.rncp.Entity

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
data class Comment(
        var comment_id: Int,
        var annoncement_id: Int,
        var user_id: Int,
        var description: String,
        var date: String,
) : PanacheEntity() {
    constructor() : this(0, 0, 0, "", "")
}
