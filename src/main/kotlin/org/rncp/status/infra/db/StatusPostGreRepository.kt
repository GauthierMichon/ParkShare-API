package org.rncp.status.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import org.rncp.status.domain.model.Status
import org.rncp.status.domain.ports.out.StatusRepository

@ApplicationScoped
class StatusPostGreRepository: PanacheRepositoryBase<StatusDAO, Int>, StatusRepository {
    override fun getById(id: Int): Status? {
        val statusDao = findById(id) ?: return null
        return statusDao.toStatus()
    }
}