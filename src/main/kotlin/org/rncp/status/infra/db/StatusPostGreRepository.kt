package org.rncp.status.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class StatusPostGreRepository: PanacheRepositoryBase<StatusDAO, Int> {
}