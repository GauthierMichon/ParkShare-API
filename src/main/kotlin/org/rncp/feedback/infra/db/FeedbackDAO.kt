package org.rncp.feedback.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.infra.db.AdDao
import org.rncp.feedback.domain.model.Feedback
import org.rncp.user.infra.db.UserDAO
import java.time.LocalDateTime

@Entity
@Table(name="feedback")
data class FeedbackDAO(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @ManyToOne(targetEntity = AdDao::class, fetch = FetchType.LAZY)
        @JoinColumn(name="ad")
        var ad: AdDao,
        @ManyToOne(targetEntity = UserDAO::class, fetch = FetchType.LAZY)
        @JoinColumn(name="user")
        var user: UserDAO,
        var rating: Int?,
        var description: String,
        var date: LocalDateTime,
) : PanacheEntityBase() {
    constructor() : this(0, AdDao(), UserDAO(), null, "", LocalDateTime.now())

    fun toFeedback(): Feedback {
        return Feedback(id, ad.id!!, user.uid, rating, description, date)
    }
}