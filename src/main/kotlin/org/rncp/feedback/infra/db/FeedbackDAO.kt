package org.rncp.feedback.infra.db

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.rncp.ad.infra.db.AdDao
import org.rncp.feedback.domain.model.Feedback
import java.time.LocalDateTime

@Entity
@Table(name="feedback")
data class FeedbackDAO(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @ManyToOne(targetEntity = AdDao::class, fetch = FetchType.LAZY)
        @JoinColumn(name="ad")
        var ad: AdDao,
        var userId: String,
        var rating: Int?,
        var description: String,
        var date: LocalDateTime,
) : PanacheEntityBase() {
    constructor() : this(0, AdDao(), "", null, "", LocalDateTime.now())

    fun toFeedback(): Feedback {
        return Feedback(id, ad.id!!, userId, rating, description, date)
    }
}